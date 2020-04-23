package com.example.soundrecorder

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.playlist_btn
import kotlinx.android.synthetic.main.activity_main.stop_icon_house
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {


    private var fileName: String = " "
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var recorder: MediaRecorder ?= null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        }else{
            false
        }
        if(!permissionToRecordAccepted) finish()
    }

    private fun startRecording(){
        recorder=
            MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(fileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                try {
                    prepare()
                }catch (e: IOException) {
                    Log.e(LOG_TAG, "prepare() failed")
                }
                start()
            }
    }

    private fun stopRecording(){
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        setContentView(R.layout.activity_main)

        var file_name = findViewById<TextView>(R.id.filename)
        var record_state = findViewById<TextView>(R.id.record_state_text)
        var record_start = findViewById<ImageButton>(R.id.record_start)
        var icon_house = findViewById<LinearLayout>(R.id.stop_icon_house)
        var playlistbtn = findViewById<ImageButton>(R.id.playlist_btn)

        record_start.apply {
            setOnClickListener(){
                startRecording()
                file_name.text = fileName
                record_state.text = "Recording..."
                icon_house.visibility = View.VISIBLE
                isClickable = false
                playlistbtn.isClickable = false

            }

        }
        stop_recording.apply {
            setOnClickListener(){
                stopRecording()
                icon_house.visibility = View.INVISIBLE
                record_start.isClickable = true
                playlistbtn.isClickable = true
                record_state.text = "Tap the microphone to start recording"

            }

        }

        playlist_btn.apply{
            setOnClickListener(){
            playlist()
            }
        }
    }

    private fun playlist(){
        var intent = Intent(applicationContext, PlaylistActivity::class.java)
        startActivity(intent)
    }


}
