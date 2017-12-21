package com.bsalazar.molonometro.entities;

import java.util.ArrayList;

/**
 * Created by bsalazar on 23/02/2017.
 */

public class Contact {

    private int UserID;
    private String UserName;
    private String Email;
    private String Name;
    private String Phone;
    private String State;
    private String ImageBase64;
    private int Molopuntos;
    private ArrayList<Integer> commonGroups;

    public Contact() {
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

    public String getImageBase64() {
        return ImageBase64;
    }

    public void setImageBase64(String imageBase64) {
        ImageBase64 = imageBase64;
    }

    public int getMolopuntos() {
        return Molopuntos;
    }

    public void setMolopuntos(int molopuntos) {
        Molopuntos = molopuntos;
    }

    public ArrayList<Integer> getCommonGroups() {
        return commonGroups;
    }

    public void setCommonGroups(ArrayList<Integer> commonGroups) {
        this.commonGroups = commonGroups;
    }
}
