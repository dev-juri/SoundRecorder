package com.example.soundrecorder

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.soundrecorder.databinding.ActivityMainBinding
import java.io.IOException
import kotlin.random.Random



class MainActivity : AppCompatActivity() {

    private var fileName: String = " "
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var recorder: MediaRecorder? = null
    private lateinit var binding: ActivityMainBinding

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
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val endRecordingStateText = getString(R.string.tap_to_record)
        val startRecording = getString(R.string.recording)

        binding.recordStart.setOnClickListener {
            fileName = "${externalCacheDir?.absolutePath}/${randomName()}.3gp"
            binding.recordCounter.base = SystemClock.elapsedRealtime()
            startRecording()
            binding.recordCounter.start()
            binding.recordStateText.text = startRecording
            toastAnAlert(startRecording)
            binding.recordStart.isClickable = false
            binding.playlistBtn.isClickable = false
            binding.stopRecording.visibility = View.VISIBLE
        }

        binding.stopRecording.setOnClickListener {
            stopRecording()
            binding.recordCounter.stop()
            binding.recordCounter.base = SystemClock.elapsedRealtime()
            toastAnAlert("New Recording: ${randomName()}.3gp saved")
            binding.recordStart.isClickable = true
            binding.playlistBtn.isClickable = true
            binding.stopRecording.visibility = View.GONE
            binding.recordStateText.text = endRecordingStateText
        }

        binding.playlistBtn.setOnClickListener {
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }
    }

    private fun randomName(): String {
        val txtPart = "Recording"
        val randomInt: Int = Random.nextInt(1000)
        fileName = "$txtPart$randomInt"
        return fileName
    }

    private fun toastAnAlert(text: String) {
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)

        return toast.show()
    }

    companion object {
        private const val LOG_TAG = "AudioRecordTest"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

}

