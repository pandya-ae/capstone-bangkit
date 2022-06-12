package com.dicoding.picodiploma.docplant.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int

    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun rotateFile(bitmap: Bitmap, currentPhotoPath: String): File {
    val file = File(currentPhotoPath)
    val exif = ExifInterface(currentPhotoPath)
    val os: OutputStream
    val orientation: Int = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )

    val rotatedBitmap: Bitmap = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(bitmap, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(bitmap, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(bitmap, 270)
        ExifInterface.ORIENTATION_NORMAL -> bitmap
        else -> bitmap
    }
    try {
        os = FileOutputStream(file)
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.flush()
        os.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return file
}