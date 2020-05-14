package com.example.soundrecorder

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.soundrecorder.database.Audio
import com.example.soundrecorder.database.AudioDatabaseDao
import kotlinx.coroutines.*

class MainActivityViewModel(
    val database: AudioDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun onStopRecord(Id: Int, filePath: String, fileName: String, Date: String){
        uiScope.launch {
            val newAudio = Audio(Id, filePath, fileName, Date)
            insert(newAudio)
            Log.i("INSERT CHECK", "$newAudio inserted")
        }
    }

    private suspend fun insert(audio: Audio){
        withContext(Dispatchers.IO){
            database.insert(audio)
        }
    }


}