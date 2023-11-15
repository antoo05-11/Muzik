package com.example.muzik.response_model;


import java.sql.Date;

public class PlayList {
    private long playListID;
    private String type;
    private long userID;
    private String name;
    private Date dateAdded;

    public PlayList(long playListID, String type, long userID, String name, Date dateAdded) {
        this.playListID = playListID;
        this.type = type;
        this.userID = userID;
        this.name = name;
        this.dateAdded = dateAdded;
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
}
