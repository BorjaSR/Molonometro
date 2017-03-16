package com.bsalazar.molonometro.rest.json;

/**
 * Created by Borja on 30/10/2016.
 */
public class UpdateUserJson {

    private int UserID;
    private String Name;
    private String State;
    private String Image;
    private String FirebaseToken;

    public UpdateUserJson() {
        this.Image = "";
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

    public String getFirebaseToken() {
        return FirebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        FirebaseToken = firebaseToken;
    }
}
