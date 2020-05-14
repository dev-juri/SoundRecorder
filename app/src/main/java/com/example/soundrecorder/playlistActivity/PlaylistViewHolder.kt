package com.example.soundrecorder.playlistActivity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.soundrecorder.database.Audio
import kotlinx.android.synthetic.main.playlist_items.view.*

class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var audioName = itemView.audio_name
    var audioDate = itemView.date_modified

    fun bind(list: Audio){
        audioName.text = list.audioName
        audioDate.text = list.date
    }

}