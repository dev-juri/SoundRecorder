package com.example.soundrecorder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.soundrecorder.model.Audio
import kotlinx.android.synthetic.main.playlist_items.view.*

class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var audioName = itemView.audio_name
    var audioSize = itemView.audio_size
    var audioDate = itemView.date_modified
    var audioLength = itemView.duration_text
    var audioProgress = itemView.progressBar
    var playBtn = itemView.state_control_button

    fun bind(list: Audio){
        audioName.text = list.audioName
        audioSize.text = "${list.size}"
        audioDate.text = list.savedDate
        playBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp)
    }

}