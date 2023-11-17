package com.example.muzik.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzik.R;
import com.example.muzik.response_model.Artist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Deprecated
public class ListArtistAdapter extends RecyclerView.Adapter<ListArtistAdapter.ArtistViewHolder> implements Filterable {
    private List<Artist> artists;
    private List<Artist> artistsOld;

    public ListArtistAdapter(List<Artist> artists) {
        this.artists = artists;
        this.artistsOld = artists;
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        private final TextView artistNameTextView;
        private final ImageView artistImageView;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistNameTextView = itemView.findViewById(R.id.tv_artist_name_item);
            artistImageView = itemView.findViewById(R.id.artist_image);
        }
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artists.get(position);
        if (artist == null) {
            return;
        }
        if (artist.getArtistID() != -1) {
            holder.artistNameTextView.setText(artist.getName());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (artists != null) return artists.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String input = constraint.toString().toLowerCase();
                if (input.isEmpty()) {
                    artists = artistsOld;
                } else {
                    List<Artist> list = new ArrayList<>();
                    for (Artist artist : artistsOld) {
                        if (Objects.requireNonNull(artist.getName()).toLowerCase().contains(input)) {
                            list.add(artist);
                        }
                    }
                    artists = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = artists;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                artists = (List<Artist>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
