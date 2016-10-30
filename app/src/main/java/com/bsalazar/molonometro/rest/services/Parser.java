package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.rest.json.UserJson;

/**
 * Created by Borja on 29/10/2016.
 */

public class Parser {

    public static User parseUser(UserJson userJson){
        User user = new User();

        user.setUserID(userJson.getUserID());
        user.setName(userJson.getName());
        user.setPhone(userJson.getPhone());
        user.setState(userJson.getState());
        user.setImageURL(userJson.getImage());
        user.setImage(Tools.getBitmapFromURL(user.getImageURL()));

        return user;
    }
}
