package com.bsalazar.molonometro.entities;

import java.util.ArrayList;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class User {

    private int UserID;
    private String UserName;
    private String Email;
    private String Name;
    private String Phone;
    private String State;
    private String ImageURL;
    private String FirebaseToken;
    private int Molopuntos;
    private int numRequest;
    private ArrayList<FriendRquest> friendRquests;

    public User() {
    }

    public int getNumRequest() {
        return numRequest;
    }

    public void setNumRequest(int numRequest) {
        this.numRequest = numRequest;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getFirebaseToken() {
        return FirebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        FirebaseToken = firebaseToken;
    }

    public int getMolopuntos() {
        return Molopuntos;
    }

    public void setMolopuntos(int molopuntos) {
        Molopuntos = molopuntos;
    }

    public ArrayList<FriendRquest> getFriendRquests() {
        return friendRquests;
    }

    public void setFriendRquests(ArrayList<FriendRquest> friendRquests) {
        this.friendRquests = friendRquests;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
