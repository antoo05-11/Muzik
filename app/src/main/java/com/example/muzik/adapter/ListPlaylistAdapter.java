package com.example.muzik.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzik.response_model.PlayList;

import java.util.List;

public class ListPlaylistAdapter extends RecyclerView.Adapter<ListSongAdapter.ViewHolder>{
    private List<PlayList> playLists;

    @NonNull
    @Override
    public ListSongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ListSongAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
