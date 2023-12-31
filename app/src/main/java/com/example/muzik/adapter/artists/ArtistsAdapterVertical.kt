package com.example.muzik.adapter.artists;

import static com.example.muzik.utils.UtilsKt.printLogcat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavHostController;
import androidx.navigation.NavOptions;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzik.R;
import com.example.muzik.data_model.standard_model.Artist;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArtistsAdapterVertical extends RecyclerView.Adapter<ArtistsAdapterVertical.ArtistViewHolder> implements Filterable {
    private List<Artist> artists;
    private final List<Artist> artistsOld;
    private final NavHostController navHostController;

    public void updateArtistList(List<Artist> artists) {
        this.artists.clear();
        this.artists.addAll(artists);
        this.notifyDataSetChanged();
    }

    public ArtistsAdapterVertical(List<Artist> artists, NavHostController navHostController) {
        this.artists = artists;
        this.artistsOld = artists;
        this.navHostController = navHostController;
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        private final TextView artistNameTextView;
        private final ImageView artistImageView;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistNameTextView = itemView.findViewById(R.id.tv_artist_name_item);
            artistImageView = itemView.findViewById(R.id.center_image_cardview);
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
            Picasso.get().load(artist.getImageURI()).into(holder.artistImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                }
            });
            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong("artistID", artist.getArtistID());
                bundle.putString("artistImageURL",
                        (artist.getImageURI() == null) ? "" : artist.getImageURI().toString());
                bundle.putString("artistName", artist.getName());

                navHostController.navigate(
                        R.id.artistFragment, bundle, new NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_in_right)
                                .setExitAnim(R.anim.slide_out_right)
                                .setPopEnterAnim(R.anim.slide_in_right)
                                .setPopExitAnim(R.anim.slide_out_right)
                                .build()
                );
            });
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
