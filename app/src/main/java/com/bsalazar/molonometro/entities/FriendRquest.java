package com.bsalazar.molonometro.entities;

import java.util.Date;

/**
 * Created by bsalazar on 20/12/17.
 */

public class FriendRquest {

    private Date date;
    private Contact contact;

    public FriendRquest() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

}
