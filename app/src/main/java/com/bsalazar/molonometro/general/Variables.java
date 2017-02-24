package com.bsalazar.molonometro.general;

import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.rest.json.CreateGroupJson;

import java.util.ArrayList;

/**
 * Created by bsalazar on 25/10/2016.
 */
public class Variables {

    public static String search_for_contacts_for_group = "";
    public static User User;

    public static ArrayList<Contact> contacts = new ArrayList<>();
    public static ArrayList<Contact> contactsWithApp = new ArrayList<>();

    public static CreateGroupJson createGroupJson;
}
