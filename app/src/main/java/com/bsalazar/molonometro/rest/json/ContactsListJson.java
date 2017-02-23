package com.bsalazar.molonometro.rest.json;

import com.bsalazar.molonometro.entities.PhoneContact;

import java.util.ArrayList;

/**
 * Created by Borja on 30/10/2016.
 */
public class ContactsListJson {

    private ArrayList<PhoneContact> contacts = new ArrayList<>();

    public ContactsListJson() {
    }

    public ArrayList<PhoneContact> getPhoneContacts() {
        return contacts;
    }

    public void setPhoneContacts(ArrayList<PhoneContact> phoneContacts) {
        this.contacts = phoneContacts;
    }
}
