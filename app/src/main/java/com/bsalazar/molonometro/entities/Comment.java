package com.bsalazar.molonometro.entities;

/**
 * Created by bsalazar on 23/03/2017.
 */
public class Comment {

    // JSON
    private int CommentID;
    private int GroupID;
    private int UserID;
    private int DestinationUserID;
    private boolean hasAnswers;
    private String Text;
    private String Image;

    // OWN
    private String UserName;
    private String UserImage;
    private String DestinationUserName;

    public Comment() {
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getDestinationUserName() {
        return DestinationUserName;
    }

    public void setDestinationUserName(String destinationUserName) {
        DestinationUserName = destinationUserName;
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

    public boolean isHasAnswers() {
        return hasAnswers;
    }

    public void setHasAnswers(boolean hasAnswers) {
        this.hasAnswers = hasAnswers;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
