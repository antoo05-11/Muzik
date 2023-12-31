package com.example.muzik.adapter.playlists

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.ui.bottom_sheet_dialog.playlists.PlaylistsBottomSheet
import com.example.muzik.ui.create_playlist_activity.CreatePlaylistActivity
import com.example.muzik.ui.playlist_album_fragment.PlaylistAlbumViewModel
import com.example.muzik.utils.printLogcat
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PlaylistsAdapterVertical(
    var listPlaylist: List<Playlist>,
    private val navHostController: NavHostController? = null,
    val fragmentOwner: Fragment? = null
) :
    RecyclerView.Adapter<PlaylistsAdapterVertical.ListPlaylistViewHolder>() {

    class ListPlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlaylistName: TextView = itemView.findViewById(R.id.tvPlaylistsName)
        val playlistImage: ImageView = itemView.findViewById(R.id.playlist_image_item)
        val playlistImageCardView: CardView =
            itemView.findViewById(R.id.card_view_playlist_image_item)
        val playlistImageShimmer: ShimmerFrameLayout =
            itemView.findViewById(R.id.card_view_playlist_image_item_shimmer)
    }

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListPlaylistViewHolder, position: Int) {
        val curPlaylist = listPlaylist[position]

        if (curPlaylist.playListID?.toInt() == -1) {
            holder.playlistImage.setBackgroundResource(R.drawable.baseline_add_box_24)
            (holder.tvPlaylistName.parent as LinearLayout)[1].visibility = View.GONE
            holder.tvPlaylistName.text = "Add more playlists!"
            holder.tvPlaylistName.textSize = 16f

            if (navHostController == null) {
                holder.playlistImage.layoutParams.apply {
                    height = 120
                    width = 120
                }
                holder.playlistImageCardView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    marginStart = 0
                }
                holder.itemView.setOnClickListener {
                    fragmentOwner?.activity?.startActivity(
                        Intent(
                            fragmentOwner.context,
                            CreatePlaylistActivity::class.java
                        )
                    )
                }
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
            if (navHostController == null) {
                holder.playlistImage.layoutParams.apply {
                    height = 120
                    width = 120
                }
                holder.tvPlaylistName.textSize = 16f
                holder.playlistImageCardView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    marginStart = 0
                }
                holder.itemView.setOnClickListener {
                    (fragmentOwner as PlaylistsBottomSheet).apply {
                        addSongToPlaylist(curPlaylist.requirePlayListID())
                    }
                }
            } else {
                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    curPlaylist.playListID?.let { it1 -> bundle.putLong("playlistAlbumID", it1) }
                    bundle.putString("playlistAlbumImageURL", curPlaylist.imageURI.toString())
                    bundle.putString("playlistAlbumName", curPlaylist.name)
                    bundle.putSerializable("type", PlaylistAlbumViewModel.Type.PLAYLIST)
                    navHostController.navigate(
                        R.id.playlistAlbumFragment, bundle, NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_right)
                            .setPopEnterAnim(R.anim.slide_in_right)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build()
                    )
                }
            }
        }
    }
}
