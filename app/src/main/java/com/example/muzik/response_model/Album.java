package com.example.muzik.response_model;

public class Album {
    private long albumID;
    private String name;
    private String imageURL;
    private long artistID;

    public Album(long albumID, String name, String imageURL, long artistID) {
        this.albumID = albumID;
        this.name = name;
        this.imageURL = imageURL;
        this.artistID = artistID;
    }

    public long getAlbumID() {
        return albumID;
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
}
