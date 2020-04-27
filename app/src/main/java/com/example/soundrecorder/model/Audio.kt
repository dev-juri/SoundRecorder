package com.example.soundrecorder.model


import android.net.Uri

data class Audio (
    val uri: Uri,
    val audioName: String,
    val savedDate: String,
    val size: Int
)