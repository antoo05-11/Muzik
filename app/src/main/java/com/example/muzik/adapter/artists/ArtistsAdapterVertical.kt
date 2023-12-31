package com.example.muzik.adapter.artists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.adapter.artists.ArtistsAdapterVertical.ArtistViewHolder
import com.example.muzik.data_model.standard_model.Artist
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.Locale

class ArtistsAdapterVertical(artists: MutableList<Artist>, navHostController: NavHostController) :
    RecyclerView.Adapter<ArtistViewHolder>(), Filterable {
    private var artists: MutableList<Artist>
    private val artistsOld: MutableList<Artist>
    private val navHostController: NavHostController

    @SuppressLint("NotifyDataSetChanged")
    fun updateArtistList(artists: List<Artist>?) {
        this.artists.clear()
        this.artists.addAll(artists!!)
        notifyDataSetChanged()
    }

    init {
        this.artists = artists
        artistsOld = artists
        this.navHostController = navHostController
    }

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistNameTextView: TextView
        val artistImageView: ImageView

        init {
            artistNameTextView = itemView.findViewById(R.id.tv_artist_name_item)
            artistImageView = itemView.findViewById(R.id.center_image_cardview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]
        if (artist.artistID != -1L) {
            holder.artistNameTextView.text = artist.name
            Picasso.get().load(artist.imageURI).into(holder.artistImageView, object : Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception) {}
            })
            holder.itemView.setOnClickListener { _: View? ->
                val bundle = Bundle()
                bundle.putLong("artistID", artist.artistID)
                bundle.putString(
                    "artistImageURL",
                    if (artist.imageURI == null) "" else artist.imageURI.toString()
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val input = constraint.toString().lowercase(Locale.getDefault())
                artists = if (input.isEmpty()) {
                    artistsOld
                } else {
                    val list: MutableList<Artist> = ArrayList()
                    for (artist in artistsOld) {
                        if (artist.requireName().lowercase(Locale.getDefault())
                                .contains(input)
                        ) {
                            list.add(artist)
                        }
                    }
                    list
                }
                val filterResults = FilterResults()
                filterResults.values = artists
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                artists = mutableListOf()
                for (i in results.values as MutableList<*>) {
                    artists.add(i as Artist)
                }
                notifyDataSetChanged()
            }
        }
    }
}