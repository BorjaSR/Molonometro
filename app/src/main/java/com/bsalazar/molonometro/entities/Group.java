package com.bsalazar.molonometro.entities;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class Group {

    private int id;
    private Bitmap image;
    private String imageBase64;
    private String name;
    private ArrayList<User> users;
    private String lastEvent;

    public Group() {
    }

    public Group(int id, String groupName) {
        this.id = id;
        name = groupName;
    }

    public Group(int id, String groupName, Bitmap groupImage) {
        this.id = id;
        image = groupImage;
        name = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(String lastEvent) {
        this.lastEvent = lastEvent;
    }
}
