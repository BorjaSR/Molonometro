package com.bsalazar.molonometro.rest.json;

import com.bsalazar.molonometro.entities.Contact;

import java.util.ArrayList;

/**
 * Created by Borja on 30/10/2016.
 */
public class ContactsListJson {

    private ArrayList<Contact> contacts = new ArrayList<>();

    public ContactsListJson() {
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
