package com.example.soundrecorder


import android.content.ContentUris
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundrecorder.model.Audio
import kotlinx.android.synthetic.main.activity_playlist.*
import java.io.File
import java.util.concurrent.TimeUnit


class PlaylistActivity : AppCompatActivity() {

    private var permissionToRecordAccepted = false

    private var permissions: Array<String> = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private lateinit var audioAdapter: AudioRecyclerAdapter

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            grantResults[0] == PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) {
            AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("Please grant ${getString(R.string.app_name)} permission to read the phone's storage.")
                .setPositiveButton("Okay") { d, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        READ_EXTERNAL_STORAGE_REQUEST
                    )
                    d.dismiss()
                }
                .setNegativeButton("Exit") { d, _ ->
                    d.dismiss()
                    finish()
                }
        } else {
            addData()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        ActivityCompat.requestPermissions(this, permissions, READ_EXTERNAL_STORAGE_REQUEST)



        initRecyclerView()
        addData()


    }

    private fun addData() {
        val data = fetchRecording()
        audioAdapter.submitList(data)
    }

    private fun initRecyclerView() {
        media_list.apply {
            layoutManager = LinearLayoutManager(this@PlaylistActivity)
            audioAdapter = AudioRecyclerAdapter()
            adapter = audioAdapter
        }
    }

    private fun fetchRecording(): List<Audio> {
        val audioList = ArrayList<Audio>()


        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.SIZE
        )

        val selection = "${MediaStore.Audio.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS).toString()
        )

        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"


        application.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val date = cursor.getString(dateColumn)
                val size = cursor.getInt(sizeColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.getContentUri("${getFolderPath()}"),
                    id
                )
                audioList += Audio(contentUri, name, date, size)
            }
        }
        Log.i("AUDIO LIST", "audioList returned $audioList")
        return audioList

    }

    companion object {
        private const val READ_EXTERNAL_STORAGE_REQUEST = 200
    }

}
fun getFolderPath(): File{
    val gpath: String = Environment.getExternalStorageDirectory().absolutePath
    val spath = "Recordings"
    return File(gpath + File.separator + spath)
}


