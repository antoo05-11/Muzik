package com.example.muzik.response_model;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.muzik.music_service.model.Album;
import com.example.muzik.music_service.model.Artist;

public class Song extends com.example.muzik.music_service.model.Song implements ResponseModel {
    private int songID;
    private Integer size;
    private String artistName;
    private Long artistID;
    private String imageURL;
    private Integer albumID;
    private String songURL;
    private int views;

    public Song(boolean isNewSample) {
        super(null, null, null);
        if (isNewSample) songID = -1;
    }

    public String getSongURL() {
        return songURL;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getArtistID() {
        return artistID;
    }

    public void setArtistID(Long artistID) {
        this.artistID = artistID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Integer albumID) {
        this.albumID = albumID;
    }

    public String getArtistName() {
        return artistName;
    }


    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @Nullable
    @Override
    public Artist getArtist() {
        return null;
    }

    @Nullable
    @Override
    public Uri getImg() {
        return null;
    }

    @Nullable
    @Override
    public Album getAlbum() {
        return null;
    }
}
