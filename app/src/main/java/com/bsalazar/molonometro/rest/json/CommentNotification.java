package com.bsalazar.molonometro.rest.json;

import com.bsalazar.molonometro.entities.Participant;

/**
 * Created by bsalazar on 3/1/18.
 */

public class CommentNotification {

    private int CommentID;
    private int GroupID;
    private Participant UserID;
    private Participant DestinationUserID;
    private String Text;
    private String Created;
    private String LastUpdate;

    public CommentNotification() {
    }

    public int getCommentID() {
        return CommentID;
    }

    public void setCommentID(int commentID) {
        CommentID = commentID;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
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

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }
}
