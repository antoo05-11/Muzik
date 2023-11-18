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
import com.example.muzik.response_model.Album;
import com.example.muzik.ui.playlist_album_fragment.PlaylistAlbumViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAlbumsHorizontalPreviewAdapter extends RecyclerView.Adapter<ListAlbumsHorizontalPreviewAdapter.AlbumPreviewHolder> {
    private final List<Album> albums;
    private final NavHostController navHostController;

    public ListAlbumsHorizontalPreviewAdapter(List<Album> albums, NavHostController navHostController) {
        this.albums = albums;
        this.navHostController = navHostController;
    }

    @NonNull
    @Override
    public AlbumPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_preview_for_horizontal_list, parent, false);
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

            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("playlistAlbumID", (int) album.getAlbumID());
                bundle.putString("playlistAlbumImageURL", album.getImageURL());
                bundle.putString("playlistAlbumName", album.getName());
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
