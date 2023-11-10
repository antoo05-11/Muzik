package com.example.muzik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.model.Song
import com.example.muzik.viewmodel.PlayerViewModel

class ListSongAdapter(private val songs: List<Song>, private val playerViewModel: PlayerViewModel) : RecyclerView.Adapter<ListSongAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song_lib, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val tvSongName = holder.itemView.findViewById<TextView>(R.id.tvSongName)
            tvSongName.text = songs[position].name
            val tvAlbumArtist = holder.itemView.findViewById<TextView>(R.id.tvAlbumArtist)
            tvAlbumArtist.text = songs[position].album + " - " + songs[position].artist
        }
        holder.itemView.setOnClickListener{
            playerViewModel.stop()
            playerViewModel.setMedia(songs[position].uri)
            playerViewModel.setSong(songs[position])
        }
    }
}