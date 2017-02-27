package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.util.ArrayList;

/**
 * Created by Borja on 29/10/2016.
 */

public class Parser {

    public static User parseUser(UserJson userJson) {
        User user = new User();

        user.setUserID(userJson.getUserID());
        user.setName(userJson.getName());
        user.setPhone(userJson.getPhone());
        user.setState(userJson.getState());
        user.setImageBase64(userJson.getImage());

        return user;
    }

    public static Contact parseContact(ContactJson contactJson) {
        Contact contact = new Contact();

        contact.setUserID(contactJson.getUserID());
        contact.setName(contactJson.getName());
        contact.setPhone(contactJson.getPhone());
        contact.setState(contactJson.getState());
        contact.setImageBase64(contactJson.getImage());
        contact.setInApp(contactJson.isInApp());

        return contact;
    }


    public static Group parseGroup(GroupJson groupJson) {
        Group group = new Group();
        group.setId(groupJson.getGroupID());
        group.setName(groupJson.getName());
        group.setImageBase64(groupJson.getImage());

        group.setUsers(new ArrayList<User>());

        return group;
    }
}
