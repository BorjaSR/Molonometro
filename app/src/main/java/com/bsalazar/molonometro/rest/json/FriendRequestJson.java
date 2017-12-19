package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 19/12/17.
 */

public class FriendRequestJson {

    private String LastUpdate;
    private UserJson User;

    public FriendRequestJson() {
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }

    public UserJson getUser() {
        return User;
    }

    public void setUser(UserJson user) {
        User = user;
    }
}
