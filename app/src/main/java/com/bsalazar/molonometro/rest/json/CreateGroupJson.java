package com.bsalazar.molonometro.rest.json;

import java.util.ArrayList;

/**
 * Created by bsalazar on 24/02/2017.
 */

public class CreateGroupJson {

    private int userID;
    private String groupName;
    private String groupImage;
    private ArrayList<Integer> contacts = new ArrayList<>();

    public CreateGroupJson() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public ArrayList<Integer> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Integer> contacts) {
        this.contacts = contacts;
    }
}
