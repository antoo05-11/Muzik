package com.example.muzik.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavHostController;
import androidx.navigation.NavOptions;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzik.R;
import com.example.muzik.data_model.standard_model.Playlist;
import com.example.muzik.ui.playlist_album_fragment.PlaylistAlbumViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListPlaylistsPreviewAdapter extends RecyclerView.Adapter<ListPlaylistsPreviewAdapter.PlaylistPreviewHolder> {
    private final List<Playlist> playlists;
    private final NavHostController navHostController;

    public ListPlaylistsPreviewAdapter(List<Playlist> playlists, NavHostController navHostController) {
        this.playlists = playlists;
        this.navHostController = navHostController;
    }

    @NonNull
    @Override
    public PlaylistPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_preview, parent, false);
        return new PlaylistPreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistPreviewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        if (playlist == null) return;
        if (playlist.getPlayListID() != -1) {
            holder.playlistPreviewImageShimmer.hideShimmer();
            holder.playlistPreviewNameShimmer.hideShimmer();
            holder.playlistPreviewNameTextView.setBackgroundColor(Color.TRANSPARENT);
            holder.playlistPreviewNameTextView.setText(playlist.getName());
            Picasso.get().load(playlist.getImageURI()).fit().into(holder.playlistPreviewImage);

            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong("playlistAlbumID", playlist.getPlayListID());
                bundle.putString("playlistAlbumImageURL", playlist.getImageURI().toString());
                bundle.putString("playlistAlbumName", playlist.getName());
                bundle.putSerializable("type", PlaylistAlbumViewModel.Type.ALBUM);
                navHostController.navigate(
                        R.id.playlistAlbumFragment, bundle, new NavOptions.Builder()
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
    public int getItemCount() {
        return playlists.size();
    }

    public static class PlaylistPreviewHolder extends RecyclerView.ViewHolder {
        ImageView playlistPreviewImage;
        TextView playlistPreviewNameTextView;
        ShimmerFrameLayout playlistPreviewImageShimmer;
        ShimmerFrameLayout playlistPreviewNameShimmer;

        public PlaylistPreviewHolder(@NonNull View itemView) {
            super(itemView);
            playlistPreviewImage = itemView.findViewById(R.id.playlist_preview_image);
            playlistPreviewNameTextView = itemView.findViewById(R.id.playlist_preview_name_tv);
            playlistPreviewImageShimmer = itemView.findViewById(R.id.shimmer_playlist_preview_image);
            playlistPreviewNameShimmer = itemView.findViewById(R.id.shimmer_playlist_preview_name_tv);
        }
    }
}
