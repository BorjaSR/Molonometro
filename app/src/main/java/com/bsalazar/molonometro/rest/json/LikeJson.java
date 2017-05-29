package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 29/05/2017.
 */

public class LikeJson {

    private int CommentID;
    private int UserID;

    public LikeJson() {
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
}
