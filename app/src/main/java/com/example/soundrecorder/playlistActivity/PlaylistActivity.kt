package com.example.soundrecorder.playlistActivity


import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundrecorder.R
import com.example.soundrecorder.database.AudioDatabase
import kotlinx.android.synthetic.main.activity_playlist.*


class PlaylistActivity : AppCompatActivity() {

    private lateinit var viewModel: PlaylistActivityViewModel

    private lateinit var audioAdapter: AudioRecyclerAdapter
    private var permissionToRecordAccepted = false

    private var permissions: Array<String> = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


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
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        ActivityCompat.requestPermissions(this, permissions,
            READ_EXTERNAL_STORAGE_REQUEST
        )

        val application = requireNotNull(this).application
        val dataSource = AudioDatabase.getInstance(application).audioDatabaseDao
        val viewModelFactory = PlaylistActivityViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlaylistActivityViewModel::class.java)


        initRecyclerView()
    }


    private fun initRecyclerView() {
        fetchData()
        media_list.apply {
            layoutManager = LinearLayoutManager(this@PlaylistActivity)
        }
    }

    private fun fetchData(){
        viewModel.audioList.observe(this, Observer {
            Log.i("List of Audio", it.toString())
            if(it.isEmpty()){
                no_record.visibility = View.VISIBLE
            }else {
                audioAdapter = AudioRecyclerAdapter(it)
                media_list.adapter = audioAdapter
            }
        })
    }


    companion object {
        private const val READ_EXTERNAL_STORAGE_REQUEST = 200
    }

}


