package com.example.soundrecorder

import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.DateFormat
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.soundrecorder.database.AudioDatabase
import com.example.soundrecorder.databinding.ActivityMainBinding
import com.example.soundrecorder.playlistActivity.PlaylistActivity
import java.io.IOException
import java.sql.Time
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private var recorder: MediaRecorder? = null
    private var fileName: String = " "

    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(android.Manifest.permission.RECORD_AUDIO)

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
            AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("Please grant ${getString(R.string.app_name)} audio recording permission to be able to use this app.")
                .setPositiveButton("Okay") { d, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        REQUEST_RECORD_AUDIO_PERMISSION
                    )
                    d.dismiss()
                }
                .setNegativeButton("Exit") { d, _ ->
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
        ActivityCompat.requestPermissions(this, permissions,
            REQUEST_RECORD_AUDIO_PERMISSION
        )
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        val endRecordingStateText: String = getString(R.string.tap_to_record)
        val startRecording: String = getString(R.string.recording)

        val application = requireNotNull(this).application
        val dataSource = AudioDatabase.getInstance(application).audioDatabaseDao
        val viewModelFactory = MainActivityViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        fileName = "${externalCacheDir!!.absolutePath}/${randomName()}.3gp"

        binding.recordStart.setOnClickListener {

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
            viewModel.onStopRecord(0, fileName, randomName(), getDate())
            stopRecording()
            binding.recordCounter.stop()
            binding.recordCounter.base = SystemClock.elapsedRealtime()

            toastAnAlert("New Recording: ${randomName()} saved")
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



    private fun toastAnAlert(text: String) {
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        return toast.show()
    }

    private fun randomName(): String {
        val txtPart = "Recording"
        val randomInt: Int = Random.nextInt(1000)
        fileName = "$txtPart _$randomInt"
        return fileName
    }
    private fun getDate(): String{
        val newDate = Date()

        return newDate.toString()
    }


    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        private const val LOG_TAG = "AudioRecordTest"
    }

}

