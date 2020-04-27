package com.example.soundrecorder

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var fileName: String = " "
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var recorder: MediaRecorder? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) {
            //TODO Don't just close the app without telling the user why with permission related issues
            AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("Please grant ${getString(R.string.app_name)} audio recording permission to be able to use this app.")
                .setPositiveButton("Okay") { d, _ ->
                    //TODO Retry permission request if user agrees
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        REQUEST_RECORD_AUDIO_PERMISSION
                    )
                    d.dismiss()
                }
                .setNegativeButton("Exit") { d, _ ->
                    //TODO Exit otherwise
                    d.dismiss()
                    finish()
                }
        }
    }

    private fun startRecording() {
        recorder =
            MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(fileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                try {
                    prepare()
                } catch (e: IOException) {
                    Log.e(LOG_TAG, "prepare() failed")
                }
                start()
            }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO Change file name to be a bit more human readable
        fileName = "${externalCacheDir?.absolutePath}/audioRecordTest.3gp"
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        setContentView(R.layout.activity_main)
        //TODO Leverage the Kotlin Android extensions since it added by default
        record_start.setOnClickListener {
            startRecording()
            filename.text = fileName
            record_state_text.text = getString(R.string.recording)
            stop_recording.visibility = View.VISIBLE
            record_start.isClickable = false
            playlist_btn.isClickable = false

        }
        stop_recording.setOnClickListener {
            stopRecording()
            stop_recording.visibility = View.GONE
            record_start.isClickable = true
            playlist_btn.isClickable = true
            record_state_text.text = getString(R.string.tap_to_record)

        }

        playlist_btn.setOnClickListener {
            startActivity(Intent(this, PlaylistActivity::class.java))
        }
    }

    //TODO Always use companion object to hold your constant values in Kotlin classes
    companion object {
        private const val LOG_TAG = "AudioRecordTest"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }
}
