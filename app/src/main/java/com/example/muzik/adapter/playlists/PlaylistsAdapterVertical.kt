package com.example.muzik.adapter.playlists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.adapter.Adapter
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.utils.printLogcat
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PlaylistsAdapterVertical(
    playlists: List<Playlist> = mutableListOf()
) : Adapter<PlaylistsAdapterVertical.PlaylistViewHolder, Playlist>(playlists) {

    private var inBottomSheet = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_playlist,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val curPlaylist = list[position]
        if (curPlaylist.playlistID?.toInt() == -1) {
            holder.playlistImage.setBackgroundResource(R.drawable.baseline_add_box_24)
            (holder.tvPlaylistName.parent as LinearLayout)[1].visibility = View.GONE
            holder.tvPlaylistName.text = "Add more playlists!"
            holder.tvPlaylistName.textSize = 16f
            holder.playlistImageShimmer.hideShimmer()
            holder.playlistImage.layoutParams.apply {
                height = 120
                width = 120
            }
            holder.playlistImageShimmer.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginStart = 0
            }
            holder.itemView.setOnClickListener {
                mainAction?.goToCreatePlaylistActivity()
            }

        } else {
            holder.tvPlaylistName.updateLayoutParams<LinearLayout.LayoutParams> { bottomMargin = 3 }
            holder.itemView.apply {
                holder.tvPlaylistName.text = curPlaylist.name

                Picasso.get().load(curPlaylist.imageURI)
                    .into(holder.playlistImage, object : Callback {
                        override fun onSuccess() {
                            holder.playlistImageShimmer.hideShimmer()
                        }

                        override fun onError(e: Exception?) {
                            printLogcat(e.toString())
                        }
                    })

            }
            if (inBottomSheet) {
                holder.playlistImage.layoutParams.apply {
                    height = 120
                    width = 120
                }
                holder.tvPlaylistName.textSize = 16f
                holder.playlistImageShimmer.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    marginStart = 0
                }
                holder.itemView.setOnClickListener {
                    mainAction?.addSongToPlaylist(curPlaylist.requirePlaylistID())
                }
            } else {
                holder.itemView.setOnClickListener {
                    mainAction?.goToPlaylistFragment(playlist = curPlaylist)
                }
            }
        }
    }

    fun setInBottomSheet(): PlaylistsAdapterVertical {
        inBottomSheet = true
        return this
    }

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlaylistName: TextView = itemView.findViewById(R.id.tvPlaylistsName)
        val playlistImage: ImageView = itemView.findViewById(R.id.playlist_image_item)
        val playlistImageCardView: CardView =
            itemView.findViewById(R.id.card_view_playlist_image_item)
        val playlistImageShimmer: ShimmerFrameLayout =
            itemView.findViewById(R.id.card_view_playlist_image_item_shimmer)
    }
}
