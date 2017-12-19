package com.bsalazar.molonometro.rest.json;

/**
 * Created by Borja on 29/10/2016.
 */
public class UserJson {

    private int UserID;
    private String UserName;
    private String Email;
    private String Password;
    private String Name;
    private String Phone;
    private String State;
    private String Image;
    private int Molopuntos;
    private int requests;
    private boolean isInApp;

    public UserJson() {
    }

    public UserJson(int userID) {
        UserID = userID;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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
}
