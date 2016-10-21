package com.bsalazar.molonometro.entities;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class Group {

    private int id;
    private Bitmap GroupImage;
    private String GroupName;
    private ArrayList<User> users;
    private String lastEvent;

    public Group() {
    }

    public Group(int id, String groupName) {
        this.id = id;
        GroupName = groupName;
    }

    public Group(int id, String groupName, Bitmap groupImage) {
        this.id = id;
        GroupImage = groupImage;
        GroupName = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getGroupImage() {
        return GroupImage;
    }

    public void setGroupImage(Bitmap groupImage) {
        GroupImage = groupImage;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
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
