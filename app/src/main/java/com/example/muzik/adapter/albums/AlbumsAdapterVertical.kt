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
import com.example.muzik.ui.fragment.main_fragment.MainAction
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class AlbumsAdapterVertical(
    albums: List<Album> = mutableListOf()
) : Adapter<AlbumsAdapterVertical.AlbumPreviewHolder, Album>(albums) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumPreviewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album_preview_for_vertical_list, parent, false)

        return AlbumPreviewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumPreviewHolder, position: Int) {
        val album = list[position]

        if (album.albumID != -1L) {

            holder.albumPreviewNameShimmer.hideShimmer()
            holder.albumPreviewNameTextView.setBackgroundColor(Color.TRANSPARENT)
            holder.albumPreviewNameTextView.text = album.name

            holder.albumMetadataTextView.text = album.artistName
            holder.albumMetadataTextView.setBackgroundColor(Color.TRANSPARENT)
            holder.albumMetadataTextViewShimmer.hideShimmer()

            Picasso.get().load(album.imageURI).fit()
                .into(holder.albumPreviewImage, object : Callback {
                    override fun onSuccess() {
                        holder.albumPreviewImageShimmer.hideShimmer()
                    }

                    override fun onError(e: Exception?) {

                    }
                })

            holder.itemView.setOnClickListener {
                (action as? MainAction)?.goToAlbumFragment(album = album)
            }
        }
    }

    class AlbumPreviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumPreviewImage: ImageView
        val albumPreviewNameTextView: TextView
        val albumPreviewImageShimmer: ShimmerFrameLayout
        val albumPreviewNameShimmer: ShimmerFrameLayout
        val albumMetadataTextView: TextView
        val albumMetadataTextViewShimmer: ShimmerFrameLayout

        init {
            albumPreviewImage = itemView.findViewById(R.id.album_vertical_preview_image)
            albumPreviewNameTextView = itemView.findViewById(R.id.album_vertical_preview_name_tv)
            albumMetadataTextView = itemView.findViewById(R.id.album_meta_data_textview)

            albumPreviewImageShimmer =
                itemView.findViewById(R.id.shimmer_album_vertical_preview_image)
            albumPreviewNameShimmer =
                itemView.findViewById(R.id.shimmer_album_vertical_preview_name_tv)
            albumMetadataTextViewShimmer =
                itemView.findViewById(R.id.shimmer_album_meta_data_textview)
        }
    }
}