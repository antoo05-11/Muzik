package com.example.muzik.adapter.artists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.adapter.Adapter
import com.example.muzik.adapter.artists.ArtistsAdapterVertical.ArtistViewHolder
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.ui.fragment.main_fragment.MainAction
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.Locale

class ArtistsAdapterVertical(artists: MutableList<Artist> = mutableListOf()) :
    Adapter<ArtistViewHolder, Artist>(artists), Filterable {
    private var artists: MutableList<Artist>
    private val artistsOld: MutableList<Artist>

    init {
        this.artists = artists
        artistsOld = artists
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
        val artist = list[position]
        if (artist.artistID != -1L) {
            holder.artistNameTextView.text = artist.name
            Picasso.get().load(artist.imageURI).into(holder.artistImageView, object : Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception) {}
            })
            holder.itemView.setOnClickListener {
                (action as? MainAction)?.goToArtistFragment(artist = artist)
            }
        }
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