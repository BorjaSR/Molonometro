package com.bsalazar.molonometro.entities;

import android.graphics.Bitmap;

import com.bsalazar.molonometro.rest.json.LastEventJson;

import java.util.ArrayList;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class Group {

    private int id;
    private Bitmap image;
    private String imageBase64;
    private String name;
    private String FirebaseTopic;
    private LastEvent lastEvent;

    private ArrayList<Participant> participants;
    private ArrayList<Comment> comments;

    public Group() {
    }

    public String getFirebaseTopic() {
        return FirebaseTopic;
    }

    public void setFirebaseTopic(String firebaseTopic) {
        FirebaseTopic = firebaseTopic;
    }

    public Group(int id, String groupName) {
        this.id = id;
        name = groupName;
    }

    public Group(int id, String groupName, Bitmap groupImage) {
        this.id = id;
        image = groupImage;
        name = groupName;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
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
