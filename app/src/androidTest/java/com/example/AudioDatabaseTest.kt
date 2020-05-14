package com.example

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.soundrecorder.database.Audio
import com.example.soundrecorder.database.AudioDatabase
import com.example.soundrecorder.database.AudioDatabaseDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AudioDatabaseTest {
    private lateinit var audioDao: AudioDatabaseDao
    private lateinit var db: AudioDatabase

    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AudioDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        audioDao = db.audioDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    @Throws
    fun insertAndGetAudio(){
        val audio = Audio(1, "storage", "RecordA", "sjs")
        audioDao.insert(audio)
    }
}