package com.bsalazar.molonometro.entities;

/**
 * Created by bsalazar on 21/03/2017.
 */
public class Participant {


    private int UserID;
    private String UserName;
    private String Name;
    private String Phone;
    private String State;
    private String Image;
    private boolean IsAdmin;
    private int Molopuntos;

    public Participant() {
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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

    public void setImage(String imageBase64) {
        Image = imageBase64;
    }

    public boolean isAdmin() {
        return IsAdmin;
    }

    public void setAdmin(boolean admin) {
        IsAdmin = admin;
    }

    public int getMolopuntos() {
        return Molopuntos;
    }

    public void setMolopuntos(int molopuntos) {
        Molopuntos = molopuntos;
    }
}
