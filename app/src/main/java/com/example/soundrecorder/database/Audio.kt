package com.example.soundrecorder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Recordings_Table")
data class Audio(

    @PrimaryKey(autoGenerate = true)
    val audioId: Int = 0,

    @ColumnInfo(name = "audio_path")
    val filePath: String,

    @ColumnInfo(name = "audio_name")
    val audioName: String,

    @ColumnInfo(name = "date_added")
    val date: String
)