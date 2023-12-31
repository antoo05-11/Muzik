package com.example.muzik.adapter.artists

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.adapter.artists.ArtistsAdapterHorizontal.ArtistPreviewHolder
import com.example.muzik.data_model.standard_model.Artist
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.Objects

class ArtistsAdapterHorizontal(
    private val artists: List<Artist>,
    private val navHostController: NavHostController
) : RecyclerView.Adapter<ArtistPreviewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistPreviewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_artist_preview, parent, false)
        return ArtistPreviewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistPreviewHolder, position: Int) {
        val artist = artists[position]
        if (artist.artistID != -1L) {
            holder.shimmerArtistNameTextView.hideShimmer()
            holder.artistNameTextView.text = artist.name
            holder.artistNameTextView.setBackgroundColor(Color.TRANSPARENT)
            Picasso.get()
                .load(artist.imageURI)
                .fit()
                .centerInside()
                .into(holder.artistPreviewImage, object : Callback {
                    override fun onSuccess() {
                        holder.shimmerArtistPreviewImage.hideShimmer()
                    }

                    override fun onError(e: Exception) {}
                })
            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putLong("artistID", artist.artistID)
                bundle.putString(
                    "artistImageURL",
                    Objects.requireNonNull(artist.imageURI).toString()
                )
                bundle.putString("artistName", artist.name)
                navHostController.navigate(
                    R.id.artistFragment, bundle, NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_right)
                        .setPopEnterAnim(R.anim.slide_in_right)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build()
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    class ArtistPreviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var artistPreviewImage: ImageView
        var artistNameTextView: TextView
        var shimmerArtistPreviewImage: ShimmerFrameLayout
        var shimmerArtistNameTextView: ShimmerFrameLayout

        init {
            artistPreviewImage = itemView.findViewById(R.id.artist_preview_image)
            artistNameTextView = itemView.findViewById(R.id.artist_preview_name_tv)
            shimmerArtistPreviewImage = itemView.findViewById(R.id.shimmer_artist_preview_image)
            shimmerArtistNameTextView = itemView.findViewById(R.id.shimmer_artist_name_textview)
        }
    }
}