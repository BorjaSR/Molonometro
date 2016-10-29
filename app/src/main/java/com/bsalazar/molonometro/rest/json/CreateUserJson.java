package com.bsalazar.molonometro.rest.json;

/**
 * Created by Borja on 29/10/2016.
 */
public class CreateUserJson {

    private String Name;
    private String Phone;
    private String State;
    private String Image;

    public CreateUserJson() {
        State = null;
        Image = null;
    }

    public CreateUserJson(String name, String phone, String state, String image) {
        Name = name;
        Phone = phone;
        State = state;
        Image = image;
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
}
