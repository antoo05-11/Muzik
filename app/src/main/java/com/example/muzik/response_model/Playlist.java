package com.example.muzik.response_model;

import com.example.muzik.data_model.standard_model.Model;

import java.util.Date;

public class Playlist implements Model {
    private long playListID;
    private String type;
    private long userID;
    private String name;
    private Date dateAdded;
    private String imageURL;

    public Playlist(boolean isNewSample) {
        if(isNewSample) playListID = -1;
    }

    public Playlist(long playListID, String type, long userID, String name, Date dateAdded, String imageURL) {
        this.playListID = playListID;
        this.type = type;
        this.userID = userID;
        this.name = name;
        if (dateAdded != null) {
            this.dateAdded = dateAdded;
        } else {
            this.dateAdded = new Date();
        }
        this.imageURL = imageURL;
    }

    public long getPlayListID() {
        return playListID;
    }

    public void setPlayListID(long playListID) {
        this.playListID = playListID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
