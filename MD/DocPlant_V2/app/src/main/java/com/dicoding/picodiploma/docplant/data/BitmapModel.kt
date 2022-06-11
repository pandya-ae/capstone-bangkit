package com.dicoding.picodiploma.docplant.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BitmapModel (
    val bitmap: Bitmap,
    val string: String,
    val path: String
        ) : Parcelable