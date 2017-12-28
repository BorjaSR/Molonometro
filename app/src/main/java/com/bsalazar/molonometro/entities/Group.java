package com.bsalazar.molonometro.entities;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class Group {

    private int id;
    private String imageURL;
    private String name;
    private String FirebaseTopic;
    private Date LastUpdate;
    private LastEvent lastEvent;

    private ArrayList<Participant> participants;
    private ArrayList<Comment> comments;

    public Group() {
    }

    public Group(int id, String groupName) {
        this.id = id;
        name = groupName;
    }

    public Date getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        LastUpdate = lastUpdate;
    }

    public String getFirebaseTopic() {
        return FirebaseTopic;
    }

    public void setFirebaseTopic(String firebaseTopic) {
        FirebaseTopic = firebaseTopic;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public LastEvent getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(LastEvent lastEvent) {
        this.lastEvent = lastEvent;
    }
}
