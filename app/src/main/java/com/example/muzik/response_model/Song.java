package com.example.muzik.response_model;

import android.net.Uri;

public class Song implements ResponseModel {
    private int songID;
    private Uri uri;
    private String name;
    private Integer duration;
    private Integer size;
    private String album;
    private String artistName;
    private Long artistID;
    private String imageURL;
    private Integer albumID;
    private String songURL;

    public Song(boolean isNewSample) {
        if(isNewSample) songID = -1;
    }

    public Song(int songID, Uri uri, String name, Integer duration, Integer size, String album, String artistName, Long artistID, String imageURL, Integer albumID) {
        this.songID = songID;
        this.name = name;
        this.uri = uri;
        this.duration = duration;
        this.size = size;
        this.album = album;
        this.artistName = artistName;
        this.artistID = artistID;
        this.imageURL = imageURL;
        this.albumID = albumID;
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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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
}
