package com.example.muzik.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzik.R;
import com.example.muzik.response_model.Song;
import com.example.muzik.ui.player_view_fragment.PlayerViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListSongsPreviewAdapter extends RecyclerView.Adapter<ListSongsPreviewAdapter.SongPreviewHolder> {
    private final List<Song> songsPreviewList;
    private PlayerViewModel playerViewModel;
    private boolean hasItemIndexTextView = false;

    public ListSongsPreviewAdapter hasItemIndexTextView() {
        this.hasItemIndexTextView = true;
        return this;
    }

    public ListSongsPreviewAdapter(List<Song> songsPreviewList) {
        this.songsPreviewList = songsPreviewList;
    }

    public void setPlayerViewModel(PlayerViewModel playerViewModel) {
        this.playerViewModel = playerViewModel;
    }

    @NonNull
    @Override
    public SongPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongPreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongPreviewHolder holder, int position) {
        Song song = songsPreviewList.get(position);
        if (song.getSongID() != -1) {
            holder.tvSongName.setText(song.getName());
            holder.artistNameSongPreviewTextview.setText(song.getArtistName());

            holder.artistNameSongPreviewTextview.setBackgroundColor(Color.TRANSPARENT);
            holder.tvSongName.setBackgroundColor(Color.TRANSPARENT);

            holder.shimmerArtistSongPreviewNameTextView.hideShimmer();
            holder.shimmerSongPreviewNameTextView.hideShimmer();

            if (!song.getImageURL().isEmpty()) {
                Picasso.get()
                        .load(song.getImageURL())
                        .fit()
                        .centerInside()
                        .into(holder.songImageItem, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.shimmerSongImageItem.hideShimmer();
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }

            if (hasItemIndexTextView) {
                holder.itemIndexTextView.setText(String.valueOf(position + 1));
            }

            holder.itemView.setOnClickListener(v -> {
                playerViewModel.stop();
                playerViewModel.setMedia(
                        (song.getUri() == null) ? Uri.parse(song.getSongURL()) : song.getUri());
                playerViewModel.setSong(song);
            });
        }
    }


    @Override
    public int getItemCount() {
        return songsPreviewList.size();
    }

    static class SongPreviewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerSongImageItem;
        ShimmerFrameLayout shimmerArtistSongPreviewNameTextView;
        ShimmerFrameLayout shimmerSongPreviewNameTextView;
        TextView artistNameSongPreviewTextview;
        TextView tvSongName;
        ImageView songImageItem;
        TextView itemIndexTextView;

        public SongPreviewHolder(@NonNull View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.song_preview_name_textview);
            shimmerSongImageItem = itemView.findViewById(R.id.shimmer_song_preview_image_item);
            shimmerArtistSongPreviewNameTextView = itemView.findViewById(R.id.shimmer_artist_name_song_preview_textview);
            shimmerSongPreviewNameTextView = itemView.findViewById(R.id.shimmer_song_preview_name_textview);
            artistNameSongPreviewTextview = itemView.findViewById(R.id.artist_name_song_preview_textview);
            songImageItem = itemView.findViewById(R.id.song_image_item);
            itemIndexTextView = itemView.findViewById(R.id.item_index_text_view);
        }
    }
}
