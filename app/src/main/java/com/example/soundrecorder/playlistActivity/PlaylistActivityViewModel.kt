package com.example.soundrecorder.playlistActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.soundrecorder.database.AudioDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class PlaylistActivityViewModel(
    val database: AudioDatabaseDao,
    application: Application
): AndroidViewModel(application) {

    private val playListModelJob = Job()
    override fun onCleared() {
        super.onCleared()
        playListModelJob.cancel()
    }
    private val uiScope = CoroutineScope(Dispatchers.Main + playListModelJob)

    val audioList = database.getAllAudios()
}