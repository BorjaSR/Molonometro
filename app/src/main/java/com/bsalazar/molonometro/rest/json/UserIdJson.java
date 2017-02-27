package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 27/02/2017.
 */

public class UserIdJson {

    private int userID;

    public UserIdJson() {
    }

    public UserIdJson(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
