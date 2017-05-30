package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 30/05/2017.
 */

public class AddUserToGroupJson {

    private int UserID;
    private int GroupID;
    private int ContactID;

    public AddUserToGroupJson() {
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public int getContactID() {
        return ContactID;
    }

    public void setContactID(int contactID) {
        ContactID = contactID;
    }
}
