package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 29/05/2017.
 */

public class LastEventJson {

    private int CommentID;
    private int UserID;
    private int DestinationUserID;
    private String LastUpdate;

    public LastEventJson() {
    }

    public int getCommentID() {
        return CommentID;
    }

    public void setCommentID(int commentID) {
        CommentID = commentID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getDestinationUserID() {
        return DestinationUserID;
    }

    public void setDestinationUserID(int destinationUserID) {
        DestinationUserID = destinationUserID;
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }
}
