package com.example.muzik.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzik.R;
import com.example.muzik.response_model.Artist;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListArtistsPreviewAdapter extends RecyclerView.Adapter<ListArtistsPreviewAdapter.ArtistPreviewHolder> {
    private List<Artist> artists;

    public ListArtistsPreviewAdapter(List<Artist> artists) {
        this.artists = artists;
    }

    @NonNull
    @Override
    public ArtistPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_preview, parent, false);
        return new ArtistPreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistPreviewHolder holder, int position) {
        Artist artist = artists.get(position);
        if (artist == null) return;
        if (artist.getArtistID() != -1) {
            holder.shimmerArtistNameTextView.hideShimmer();
            holder.artistNameTextView.setText(artist.getName());
            holder.artistNameTextView.setBackgroundColor(Color.TRANSPARENT);

            if (artist.getImageURL() != null) {
                Picasso.get()
                        .load(artist.getImageURL())
                        .fit()
                        .centerInside()
                        .into(holder.artistPreviewImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.shimmerArtistPreviewImage.hideShimmer();
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
        }

    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    static class ArtistPreviewHolder extends RecyclerView.ViewHolder {
        ImageView artistPreviewImage;
        TextView artistNameTextView;
        ShimmerFrameLayout shimmerArtistPreviewImage;
        ShimmerFrameLayout shimmerArtistNameTextView;

        public ArtistPreviewHolder(@NonNull View itemView) {
            super(itemView);
            artistPreviewImage = itemView.findViewById(R.id.artist_preview_image);
            artistNameTextView = itemView.findViewById(R.id.artist_preview_name_tv);
            shimmerArtistPreviewImage = itemView.findViewById(R.id.shimmer_artist_preview_image);
            shimmerArtistNameTextView = itemView.findViewById(R.id.shimmer_artist_name_textview);
        }
    }
}
