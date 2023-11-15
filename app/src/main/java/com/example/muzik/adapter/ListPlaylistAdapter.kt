package com.example.muzik.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.response_model.Playlist
import com.squareup.picasso.Picasso

class ListPlaylistAdapter(private val listPlaylist: List<Playlist>) : RecyclerView.Adapter<ListPlaylistAdapter.ListPlaylistViewHolder>() {

    class ListPlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlaylistViewHolder {
        return ListPlaylistViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_playlist,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listPlaylist.size
    }

    override fun onBindViewHolder(holder: ListPlaylistViewHolder, position: Int) {
        val curPlaylist = listPlaylist[position]
        val tvPlaylistName: TextView = holder.itemView.findViewById(R.id.tvPlaylistsName)
        val playlistImage: ImageView = holder.itemView.findViewById(R.id.playlist_image_item)

        holder.itemView.apply {
            tvPlaylistName.text = curPlaylist.name

            if (curPlaylist.imageURL.isNotEmpty()) {
                Picasso.get()
                    .load(curPlaylist.imageURL)
                    .fit()
                    .centerInside()
                    .into(playlistImage)
            }
        }

    }


}
