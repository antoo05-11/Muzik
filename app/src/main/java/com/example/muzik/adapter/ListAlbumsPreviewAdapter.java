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
import com.example.muzik.response_model.Album;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAlbumsPreviewAdapter extends RecyclerView.Adapter<ListAlbumsPreviewAdapter.AlbumPreviewHolder> {
    private final List<Album> albums;

    public ListAlbumsPreviewAdapter(List<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_preview, parent, false);
        return new AlbumPreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumPreviewHolder holder, int position) {
        Album album = albums.get(position);
        if (album == null) return;
        if (album.getAlbumID() != -1) {
            holder.albumPreviewImageShimmer.hideShimmer();

            holder.albumPreviewNameShimmer.hideShimmer();
            holder.albumPreviewNameTextView.setBackgroundColor(Color.TRANSPARENT);
            holder.albumPreviewNameTextView.setText(album.getName());

            Picasso.get().load(album.getImageURL()).fit().into(holder.albumPreviewImage);
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class AlbumPreviewHolder extends RecyclerView.ViewHolder {
        ImageView albumPreviewImage;
        TextView albumPreviewNameTextView;
        ShimmerFrameLayout albumPreviewImageShimmer;
        ShimmerFrameLayout albumPreviewNameShimmer;

        public AlbumPreviewHolder(@NonNull View itemView) {
            super(itemView);
            albumPreviewImage = itemView.findViewById(R.id.album_preview_image);
            albumPreviewNameTextView = itemView.findViewById(R.id.album_preview_name_tv);
            albumPreviewImageShimmer = itemView.findViewById(R.id.shimmer_album_preview_image);
            albumPreviewNameShimmer = itemView.findViewById(R.id.shimmer_album_preview_name_tv);
        }
    }
}
