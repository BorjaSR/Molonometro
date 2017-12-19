package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 19/12/17.
 */

public class RequestFriendJson {

    private int UserID;
    private int FriendID;

    public RequestFriendJson() {
    }

    public RequestFriendJson(int userID, int friendID) {
        UserID = userID;
        FriendID = friendID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getFriendID() {
        return FriendID;
    }

    public void setFriendID(int friendID) {
        FriendID = friendID;
    }
}
