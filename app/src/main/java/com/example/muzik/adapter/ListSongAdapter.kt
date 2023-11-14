package com.example.muzik.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.response_model.Song
import com.example.muzik.viewmodel.PlayerViewModel

class ListSongAdapter(private val songs: List<Song>, private val playerViewModel: PlayerViewModel) :
    RecyclerView.Adapter<ListSongAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val tvSongName = holder.itemView.findViewById<TextView>(R.id.tvSongName)
            tvSongName.text = songs[position].name
            val tvArtistUnderSongItem =
                holder.itemView.findViewById<TextView>(R.id.tv_artist_under_song_item)
            tvArtistUnderSongItem.text =
                String.format(songs[position].album + " - " + songs[position].artistName)
            if (songs[position].album == null) {
                tvArtistUnderSongItem.text = String.format(songs[position].artistName)
            }
        }
        holder.itemView.setOnClickListener {
            playerViewModel.stop()
            playerViewModel.setMedia(if (songs[position].uri == null) Uri.parse(songs[position].songURL) else songs[position].uri)
            playerViewModel.setSong(songs[position])
        }
    }
}