package com.bsalazar.molonometro.general;

import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bsalazar on 25/10/2016.
 */
public class Variables {

    public static ArrayList<Contact> contacts = new ArrayList<>();
    public static String search_for_contacts_for_group = "";
    public static User User;
    public static ArrayList<UserJson> contacts2 = new ArrayList<>();
    public static ArrayList<UserJson> contactsWithApp = new ArrayList<>();
}
