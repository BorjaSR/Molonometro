package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 27/02/2017.
 */
public class GroupJson {

    private int GroupID;
    private String Name;
    private String Image;
    private String FirebaseTopic;
    private String LastUpdate;
    private LastEventJson lastEvent;

    public GroupJson() {
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }

    public String getFirebaseTopic() {
        return FirebaseTopic;
    }

    public LastEventJson getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(LastEventJson lastEvent) {
        this.lastEvent = lastEvent;
    }

    public void setFirebaseTopic(String firebaseTopic) {
        FirebaseTopic = firebaseTopic;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
