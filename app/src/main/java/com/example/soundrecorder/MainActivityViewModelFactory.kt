package com.example.soundrecorder

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.soundrecorder.database.AudioDatabaseDao

class MainActivityViewModelFactory(
    private val dataSource: AudioDatabaseDao,
    private val application: Application
): ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
        }
}