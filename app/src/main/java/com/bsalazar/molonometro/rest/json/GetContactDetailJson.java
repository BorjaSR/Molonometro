package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 08/09/2017.
 */

public class GetContactDetailJson {

    private int ContactID;
    private int UserID;

    public GetContactDetailJson() {
    }

    public int getContactID() {
        return ContactID;
    }

    public void setContactID(int contactID) {
        ContactID = contactID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
