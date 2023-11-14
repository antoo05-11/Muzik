package com.example.muzik.response_model;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private long artistID;
    private String name;
    private List<Song> songs;
    private String imageURL;

    public Artist() {
    }

    public Artist(long artistID, String name) {
        this.artistID = artistID;
        this.name = name;
        songs = new ArrayList<>();
    }

    public Artist(long artistID, String name, List<Song> songs, String imageURL) {
        this.artistID = artistID;
        this.name = name;
        this.songs = songs;
        this.imageURL = imageURL;
    }

    public long getArtistID() {
        return artistID;
    }

    public void setArtistID(long artistID) {
        this.artistID = artistID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
