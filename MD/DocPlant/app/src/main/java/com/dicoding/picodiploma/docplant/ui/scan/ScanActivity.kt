package com.dicoding.picodiploma.docplant.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.picodiploma.docplant.R
import com.dicoding.picodiploma.docplant.databinding.ActivityScanBinding
import com.dicoding.picodiploma.docplant.ui.main.MainActivity
import com.dicoding.picodiploma.docplant.ui.setting.SettingActivity
import com.dicoding.picodiploma.docplant.util.createCustomTempFile
import com.dicoding.picodiploma.docplant.util.uriToFile
import java.io.File

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var getFile: File? = null

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigation()
        setupPermission()
        setupAction()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this@ScanActivity,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun setupAction() {
        binding.apply {
            btnCamera.setOnClickListener { startTakePhoto() }
            btnGallery.setOnClickListener { startGallery() }
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@ScanActivity,
                "com.dicoding.picodiploma.docplant",
                it
            )
            currentPhotoPath = it.absolutePath

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@ScanActivity)
            getFile = myFile

        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun setNavigation() {
        val navigation = binding.navView
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Navigation_Home -> {
                    onMenuItemSelected(R.id.Navigation_Home, item)
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0,0)
                }

                R.id.Navigation_Scan -> { }

                R.id.Navigation_Setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    overridePendingTransition(0,0)
                }
            }
            false
        }
    }
}