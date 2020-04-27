package com.example.soundrecorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_playlist.*

class PlaylistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        //TODO Remove unneeded call to apply when only one method/attribute is called/altered
        recorder_activity.setOnClickListener{
                //TODO avoid calling applicationContext unless you need to
                startActivity(Intent(this, MainActivity::class.java))
            }
    }
}
