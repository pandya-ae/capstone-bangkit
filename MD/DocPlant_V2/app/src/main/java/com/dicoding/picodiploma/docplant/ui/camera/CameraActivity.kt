package com.dicoding.picodiploma.docplant.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.docplant.data.BitmapModel
import com.dicoding.picodiploma.docplant.databinding.ActivityCameraBinding
import com.dicoding.picodiploma.docplant.ml.ModelBaruFix
import com.dicoding.picodiploma.docplant.ui.ResultActivity
import com.dicoding.picodiploma.docplant.ui.camera.cameraX.CustomCameraActivity
import com.dicoding.picodiploma.docplant.ui.information.InformationActivity
import com.dicoding.picodiploma.docplant.utils.rotateBitmap
import com.dicoding.picodiploma.docplant.utils.uriToFile
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var getFile: File? = null
    private lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap
    var result = " "

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val RESULT = " "
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnInformation.setOnClickListener{
                startActivity(Intent(this@CameraActivity, InformationActivity::class.java))
            }
            btnCamera.setOnClickListener { startCameraX() }
            btnGallery.setOnClickListener { startGallery() }
            btnUpload.setOnClickListener {
                val diseaseResult = BitmapModel(
                    bitmap,
                    result
                )
                val moveWithObjectIntent = Intent(this@CameraActivity, ResultActivity::class.java)
                moveWithObjectIntent.putExtra(ResultActivity.EXTRA_RESULT, diseaseResult)
                startActivity(moveWithObjectIntent)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@CameraActivity)
            getFile = myFile

//            binding.previewImageView.setImageURI(selectedImg)
            bitmap =
                BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImg))
            binding.previewImageView.setImageBitmap(bitmap)
            DiseaseModel(bitmap)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CustomCameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.previewImageView.setImageBitmap(result)
            bitmap = result
            DiseaseModel(bitmap)
        }
    }

    private fun DiseaseModel(bitmap: Bitmap) {
        val name_file = "label_baru.txt"
        val label = application.assets.open(name_file).bufferedReader().use { it.readText() }
        val labels = label.split("\n")
        val model = ModelBaruFix.newInstance(this)
        var bitmapscale = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
        binding.previewImageView.setImageBitmap(bitmapscale)
        Log.e("Bitmap", bitmapscale.toString())

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmapscale)

        val byteBuffer = tensorImage.buffer
        Log.d("shape", byteBuffer.toString())
        Log.d("shape", inputFeature0.buffer.toString())
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        Log.e("outputs", outputs.toString())
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val max = getMax(outputFeature0.floatArray, outputFeature0.floatArray.size)
        Log.e("outputGenerator: ", "-----------------------")
        Log.e("outputGenerator: ", outputFeature0.floatArray.toList().toString())
        Log.e("outputGenerator: ", max.toString())
        Log.e("outputGenerator: ", outputFeature0.floatArray.size.toString())
        Log.e("outputGenerator: ", outputFeature0.dataType.toString())
        Log.e("outputGenerator: ", outputFeature0.dataType.toString())
        result = labels[max]
        model.close()
    }

    fun getMax(arr: FloatArray, size: Int): Int {
        var ind = 0;
        var min = 0.0f;

        for (i in 0 until size) {
            Log.e("get: ", i.toString())
            Log.e("get i: ", arr[i].toString())
            if (arr[i] > min) {
                Log.e("getMax: ", i.toString())
                Log.e("getMax: ", arr[i].toString())

                min = arr[i]
                ind = i;
            }
        }
        return ind
    }
}