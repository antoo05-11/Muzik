package com.example.muzik.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.response_model.Song
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@Deprecated("Use ListSongPreviewAdapter instead.")
class ListSongAdapter(private val songs: List<Song>, private val playerViewModel: PlayerViewModel) :
    RecyclerView.Adapter<ListSongAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        if (song.songID != -1) {
            holder.itemView.apply {
                val tvSongName =
                    holder.itemView.findViewById<TextView>(R.id.song_preview_name_textview)
                tvSongName.text = song.name

                val shimmerSongImageItem =
                    holder.itemView.findViewById<ShimmerFrameLayout>(R.id.shimmer_song_preview_image_item)

                val shimmerArtistSongPreviewNameTextView =
                    holder.itemView.findViewById<ShimmerFrameLayout>(R.id.shimmer_artist_name_song_preview_textview)
                shimmerArtistSongPreviewNameTextView.hideShimmer()

                val shimmerSongPreviewNameTextView =
                    holder.itemView.findViewById<ShimmerFrameLayout>(R.id.shimmer_song_preview_name_textview)
                shimmerSongPreviewNameTextView.hideShimmer()

                val artistNameSongPreviewTextview =
                    holder.itemView.findViewById<TextView>(R.id.artist_name_song_preview_textview)
                artistNameSongPreviewTextview.text = String.format(songs[position].artistName)

                val songImageItem = holder.itemView.findViewById<ImageView>(R.id.song_image_item)

                if (songs[position].imageURL.isNotEmpty()) {
                    Picasso.get()
                        .load(songs[position].imageURL)
                        .fit()
                        .centerInside()
                        .into(songImageItem, object : Callback {
                            override fun onSuccess() {
                                shimmerSongImageItem.hideShimmer()
                            }

                            override fun onError(e: Exception) {}

                        })
                }
            }
            holder.itemView.setOnClickListener {
                playerViewModel.stop()
                playerViewModel.setMedia(if (songs[position].uri == null) Uri.parse(songs[position].songURL) else songs[position].uri)
                playerViewModel.setSong(songs[position])
            }
        }

    }
}