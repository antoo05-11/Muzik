package com.example.muzik.adapter.playlists

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavHostController
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.adapter.playlists.PlaylistsAdapterHorizontal.PlaylistPreviewHolder
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.ui.playlist_album_fragment.PlaylistAlbumViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PlaylistsAdapterHorizontal(
    private val playlists: List<Playlist>,
    private val navHostController: NavHostController
) : RecyclerView.Adapter<PlaylistPreviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistPreviewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_preview, parent, false)
        return PlaylistPreviewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistPreviewHolder, position: Int) {
        val playlist = playlists[position]
        if (playlist.playListID != -1L) {

            holder.playlistPreviewNameShimmer.hideShimmer()
            holder.playlistPreviewNameTextView.setBackgroundColor(Color.TRANSPARENT)
            holder.playlistPreviewNameTextView.text = playlist.name

            Picasso.get().load(playlist.imageURI).fit()
                .into(holder.playlistPreviewImage, object : Callback {
                    override fun onSuccess() {
                        holder.playlistPreviewImageShimmer.hideShimmer()
                    }

                    override fun onError(e: Exception?) {
                    }

                })

            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putLong("playlistAlbumID", playlist.playListID!!)
                bundle.putString("playlistAlbumImageURL", playlist.imageURI.toString())
                bundle.putString("playlistAlbumName", playlist.name)
                bundle.putSerializable("type", PlaylistAlbumViewModel.Type.PLAYLIST)
                navHostController.navigate(
                    R.id.playlistAlbumFragment, bundle
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    class PlaylistPreviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var playlistPreviewImage: ImageView
        var playlistPreviewNameTextView: TextView
        var playlistPreviewImageShimmer: ShimmerFrameLayout
        var playlistPreviewNameShimmer: ShimmerFrameLayout

        init {
            playlistPreviewImage = itemView.findViewById(R.id.playlist_preview_image)
            playlistPreviewNameTextView = itemView.findViewById(R.id.playlist_preview_name_tv)
            playlistPreviewImageShimmer = itemView.findViewById(R.id.shimmer_playlist_preview_image)
            playlistPreviewNameShimmer =
                itemView.findViewById(R.id.shimmer_playlist_preview_name_tv)
        }
    }
}