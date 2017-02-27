package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 27/02/2017.
 */
public class GroupJson {

    private int GroupID;
    private String Name;
    private String Image;

    public GroupJson() {
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
