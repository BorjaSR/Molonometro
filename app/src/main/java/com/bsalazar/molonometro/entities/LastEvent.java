package com.bsalazar.molonometro.entities;

import java.util.Date;

/**
 * Created by bsalazar on 29/05/2017.
 */

public class LastEvent {

    private int CommentID;
    private int UserID;
    private String UserName;
    private int DestinationUserID;
    private String DestinationUserName;
    private Date LastUpdate;

    public LastEvent() {
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getDestinationUserID() {
        return DestinationUserID;
    }

    public void setDestinationUserID(int destinationUserID) {
        DestinationUserID = destinationUserID;
    }

    public String getDestinationUserName() {
        return DestinationUserName;
    }

    public void setDestinationUserName(String destinationUserName) {
        DestinationUserName = destinationUserName;
    }

    public Date getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        LastUpdate = lastUpdate;
    }
}
