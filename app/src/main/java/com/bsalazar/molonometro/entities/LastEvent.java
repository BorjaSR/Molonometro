package com.bsalazar.molonometro.entities;

import java.util.Date;

/**
 * Created by bsalazar on 29/05/2017.
 */

public class LastEvent {

    private int CommentID;
    private Participant User;
    private Participant DestinationUser;
    private Date LastUpdate;

    public LastEvent() {
    }

    public int getCommentID() {
        return CommentID;
    }

    public void setCommentID(int commentID) {
        CommentID = commentID;
    }

    public Participant getUser() {
        return User;
    }

    public void setUser(Participant user) {
        User = user;
    }

    public Participant getDestinationUser() {
        return DestinationUser;
    }

    public void setDestinationUser(Participant destinationUser) {
        DestinationUser = destinationUser;
    }

    public Date getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        LastUpdate = lastUpdate;
    }
}
