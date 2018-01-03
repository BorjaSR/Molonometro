package com.bsalazar.molonometro.rest.json;

import com.bsalazar.molonometro.entities.Participant;

/**
 * Created by bsalazar on 29/05/2017.
 */

public class LastEventJson {

    private int CommentID;
    private Participant UserID;
    private Participant DestinationUserID;
    private String LastUpdate;

    public LastEventJson() {
    }

    public int getCommentID() {
        return CommentID;
    }

    public void setCommentID(int commentID) {
        CommentID = commentID;
    }

    public Participant getUserID() {
        return UserID;
    }

    public void setUserID(Participant userID) {
        UserID = userID;
    }

    public Participant getDestinationUserID() {
        return DestinationUserID;
    }

    public void setDestinationUserID(Participant destinationUserID) {
        DestinationUserID = destinationUserID;
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }
}
