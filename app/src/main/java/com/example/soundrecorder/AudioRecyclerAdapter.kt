package com.example.soundrecorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soundrecorder.model.Audio

class AudioRecyclerAdapter: RecyclerView.Adapter<PlaylistViewHolder>(){
    private var items: List<Audio> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun submitList(audioList: List<Audio>){
        items = audioList
    }

}