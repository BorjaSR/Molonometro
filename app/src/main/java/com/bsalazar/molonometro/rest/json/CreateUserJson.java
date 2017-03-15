package com.bsalazar.molonometro.rest.json;

/**
 * Created by Borja on 29/10/2016.
 */
public class CreateUserJson {

    private String Name;
    private String Phone;
    private String State;
    private String Image;
    private String FirebaseToken;

    public CreateUserJson() {
        State = "";
        Image = "";
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

    public String getFirebaseToken() {
        return FirebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        FirebaseToken = firebaseToken;
    }
}
