package com.example.soundrecorder.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AudioDatabaseDao {

    @Insert
    fun insert(audio: Audio)

    @Update
    fun update(audio: Audio)

    @Delete
    fun deleteAudio(audio: Audio)

    @Query("DELETE FROM Recordings_Table")
    fun deleteAllAudios()

    @Query("SELECT * FROM Recordings_Table ORDER BY audioId DESC")
    fun getAllAudios(): LiveData<List<Audio>>

}