package com.example.soundrecorder.model


import android.icu.util.TimeUnit
import android.net.Uri
import android.provider.MediaStore

data class DataModel (
    val uri: Uri,
    val audioName: String,
    val audioDuration: Int,
    val savedDate: Int,
    val size: Int
)