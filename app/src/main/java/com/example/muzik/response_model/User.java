package com.example.muzik.response_model;

import com.example.muzik.data_model.standard_model.Model;

import java.util.Date;

public class User implements Model {
    private long userID;
    private String name;
    private String email;
    private String password;
    private Date dateOfBirth;
    private Integer phoneNumber;

    public User(boolean isNewSample) {
        if(isNewSample) userID = -1;
    }

    public User (String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(long userID, String name, String email, Integer phoneNumber, Date dateOfBirth) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phoneNumber= phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public User(long userID, String name, String email, String password) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
