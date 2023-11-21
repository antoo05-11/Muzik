package com.example.muzik.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.muzik.R;
import com.example.muzik.response_model.Song;
import com.example.muzik.ui.player_view_fragment.PlayerViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ListSongsPreviewAdapter extends RecyclerView.Adapter<ListSongsPreviewAdapter.SongPreviewHolder> {
    private final List<Song> songsPreviewList;
    private PlayerViewModel playerViewModel;
    private boolean hasItemIndexTextView = false;
    private boolean hasViewsShowed = false;
    private Fragment fragmentOwner;
    LottieAnimationView playingGifView;

    public ListSongsPreviewAdapter hasItemIndexTextView() {
        this.hasItemIndexTextView = true;
        return this;
    }

    public ListSongsPreviewAdapter hasViewsShowed() {
        this.hasViewsShowed = true;
        return this;
    }

    public ListSongsPreviewAdapter setFragmentOwner(Fragment fragmentOwner) {
        this.fragmentOwner = fragmentOwner;
        return this;
    }

    public ListSongsPreviewAdapter(List<Song> songsPreviewList) {
        this.songsPreviewList = songsPreviewList;
    }

    public ListSongsPreviewAdapter setPlayerViewModel(PlayerViewModel playerViewModel) {
        this.playerViewModel = playerViewModel;
        return this;
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
            if (!hasViewsShowed)
                holder.artistNameSongPreviewTextview.setText(song.getArtistName());
            else {
                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                holder.artistNameSongPreviewTextview.setText(numberFormat.format(song.getViews()));
            }

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

            if (playerViewModel.getSongMutableLiveData().getValue() != null &&
                    playerViewModel.getSongMutableLiveData().getValue().getSongID() == song.getSongID()) {
                setPlayingEffect(holder);
            }

            holder.itemView.setOnClickListener(v -> {
                if (playerViewModel.getSongMutableLiveData().getValue() == null ||
                        playerViewModel.getSongMutableLiveData().getValue().getSongID() != song.getSongID()) {
                    playerViewModel.stop();
                    playerViewModel.setMedia(
                            (song.getUri() == null) ? Uri.parse(song.getSongURL()) : song.getUri());
                    playerViewModel.setSong(song);

                    setPlayingEffect(holder);
                }
            });
        }
    }

    private void setPlayingEffect(SongPreviewHolder holder) {
        if (playingGifView != null) {
            ((TextView) ((LinearLayout) playingGifView.getParent()).findViewById(R.id.song_preview_name_textview))
                    .setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            ((LinearLayout) playingGifView.getParent()).removeView(playingGifView);
        }

        playingGifView = new LottieAnimationView(fragmentOwner.requireContext());
        int widthInDp = 20;
        int heightInDp = 20;
        int rightMarginInDp = 10;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInDp, fragmentOwner.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightInDp, fragmentOwner.getResources().getDisplayMetrics())
        );
        layoutParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightMarginInDp, fragmentOwner.getResources().getDisplayMetrics());
        playingGifView.setRepeatCount(LottieDrawable.INFINITE);
        playingGifView.setLayoutParams(layoutParams);
        playingGifView.setAnimation(R.raw.playing_gif);
        playingGifView.playAnimation();
        ((LinearLayout) holder.tvSongName.getParent().getParent()).addView(playingGifView, 0);

        holder.tvSongName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }


    @Override
    public int getItemCount() {
        return songsPreviewList.size();
    }

    public static class SongPreviewHolder extends RecyclerView.ViewHolder {
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
            tvSongName.setSelected(true);
        }
    }
}
