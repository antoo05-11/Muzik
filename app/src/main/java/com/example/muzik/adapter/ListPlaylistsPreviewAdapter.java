package com.example.muzik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzik.R;
import com.example.muzik.response_model.Playlist;

import java.util.List;

public class ListPlaylistsPreviewAdapter extends RecyclerView.Adapter<ListPlaylistsPreviewAdapter.PlaylistPreviewHolder> {
    private final List<Playlist> playlists;

    public ListPlaylistsPreviewAdapter(List<Playlist> playlists) {
        this.playlists = playlists;
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
        holder.playlistPreviewNameTextView.setText(playlist.getName());
        holder.playlistPreviewImage.setImageResource(R.drawable._30px_adele_21);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class PlaylistPreviewHolder extends RecyclerView.ViewHolder {
        ImageView playlistPreviewImage;
        TextView playlistPreviewNameTextView;

        public PlaylistPreviewHolder(@NonNull View itemView) {
            super(itemView);
            playlistPreviewImage = itemView.findViewById(R.id.playlist_preview_image);
            playlistPreviewNameTextView = itemView.findViewById(R.id.playlist_preview_name_tv);
        }
    }
}
