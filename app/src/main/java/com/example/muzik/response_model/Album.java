package com.example.muzik.response_model;

import android.net.Uri;

import java.util.List;

public class Album implements ResponseModel {
    private long albumID;
    private String name;
    private Uri albumArtURI;
    private String imageURL;
    private long artistID;
    private String albumArtist;

    private List<Song> songs;

    public Album(boolean isNewSample) {
       if(isNewSample) albumID = -1;
    }

    public Album(long albumID, String name, String albumArtist, Uri albumArtURI, List<Song> songs) {
        this.albumID = albumID;
        this.name = name;
        this.albumArtURI = albumArtURI;
        this.songs = songs;
    }

    public Album(long albumID, String name, String imageURL, long artistID, List<Song> songs) {
        this.albumID = albumID;
        this.name = name;
        this.imageURL = imageURL;
        this.artistID = artistID;
        this.songs = songs;
    }

    public long getAlbumID() {
        return albumID;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getArtistID() {
        return artistID;
    }

    public void setArtistID(long artistID) {
        this.artistID = artistID;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Uri getAlbumArtURI() {
        return albumArtURI;
    }

    public void setAlbumArtURI(Uri albumArtURI) {
        this.albumArtURI = albumArtURI;
    }
}
