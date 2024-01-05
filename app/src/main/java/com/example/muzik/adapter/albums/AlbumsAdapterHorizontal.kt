package com.example.muzik.adapter.albums

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.adapter.Adapter
import com.example.muzik.data_model.standard_model.Album
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso

class AlbumsAdapterHorizontal(
    albums: List<Album>
) : Adapter<AlbumsAdapterHorizontal.AlbumPreviewHolder, Album>(albums) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumPreviewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album_preview_for_horizontal_list, parent, false)
        return AlbumPreviewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumPreviewHolder, position: Int) {
        val album = list?.get(position) ?: return
        if (album.albumID != -1L) {
            holder.albumPreviewImageShimmer.hideShimmer()
            holder.albumPreviewNameShimmer.hideShimmer()
            holder.albumPreviewNameTextView.setBackgroundColor(Color.TRANSPARENT)
            holder.albumPreviewNameTextView.text = album.name
            Picasso.get().load(album.imageURI).fit().into(holder.albumPreviewImage)
            holder.itemView.setOnClickListener {
                mainAction?.goToAlbumFragment(album = album)
            }
        }
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