package com.bsalazar.molonometro.rest.json;

import java.util.ArrayList;

/**
 * Created by Borja on 29/10/2016.
 */
public class ContactJson {

    private int UserID;
    private String Name;
    private String Phone;
    private String State;
    private String Image;
    private boolean isInApp;
    private int Molopuntos;
    private ArrayList<Integer> commonGroups;

    public ContactJson() {
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public boolean isInApp() {
        return isInApp;
    }

    public void setInApp(boolean inApp) {
        isInApp = inApp;
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
