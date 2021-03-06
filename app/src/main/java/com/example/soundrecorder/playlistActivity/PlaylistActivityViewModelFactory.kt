package com.example.soundrecorder.playlistActivity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.soundrecorder.database.AudioDatabaseDao

class PlaylistActivityViewModelFactory (
    private val dataSource: AudioDatabaseDao,
    private val application: Application
): ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(PlaylistActivityViewModel::class.java)){
            return PlaylistActivityViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}