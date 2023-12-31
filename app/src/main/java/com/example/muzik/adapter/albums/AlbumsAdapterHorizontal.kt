package com.example.muzik.adapter.albums

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
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.ui.playlist_album_fragment.PlaylistAlbumViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import java.util.Objects

class AlbumsAdapterHorizontal(
    private val albums: List<Album>,
    private val navHostController: NavHostController
) : RecyclerView.Adapter<AlbumsAdapterHorizontal.AlbumPreviewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumPreviewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album_preview_for_horizontal_list, parent, false)
        return AlbumPreviewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumPreviewHolder, position: Int) {
        val album = albums[position]
        if (album.albumID != -1L) {
            holder.albumPreviewImageShimmer.hideShimmer()
            holder.albumPreviewNameShimmer.hideShimmer()
            holder.albumPreviewNameTextView.setBackgroundColor(Color.TRANSPARENT)
            holder.albumPreviewNameTextView.text = album.name
            Picasso.get().load(album.imageURI).fit().into(holder.albumPreviewImage)
            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putLong("playlistAlbumID", album.albumID!!)
                bundle.putString(
                    "playlistAlbumImageURL",
                    Objects.requireNonNull(album.imageURI).toString()
                )
                bundle.putString("playlistAlbumName", album.name)
                bundle.putString("albumArtistName", album.artistName)
                bundle.putSerializable("type", PlaylistAlbumViewModel.Type.ALBUM)
                navHostController.navigate(
                    R.id.playlistAlbumFragment, bundle
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class AlbumPreviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var albumPreviewImage: ImageView
        var albumPreviewNameTextView: TextView
        var albumPreviewImageShimmer: ShimmerFrameLayout
        var albumPreviewNameShimmer: ShimmerFrameLayout

        init {
            albumPreviewImage = itemView.findViewById(R.id.album_preview_image)
            albumPreviewNameTextView = itemView.findViewById(R.id.album_preview_name_tv)
            albumPreviewImageShimmer = itemView.findViewById(R.id.shimmer_album_preview_image)
            albumPreviewNameShimmer = itemView.findViewById(R.id.shimmer_album_preview_name_tv)
        }
    }
}