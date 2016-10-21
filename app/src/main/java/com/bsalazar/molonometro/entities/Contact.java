package com.bsalazar.molonometro.entities;

import android.graphics.Bitmap;

/**
 * Created by bsalazar on 18/10/2016.
 */
public class Contact {

    private Bitmap userIcon;
    private String phoneDisplayName;
    private String phoneNumber;

    public Contact(Bitmap userIcon, String phoneDisplayName, String phoneNumber) {
        this.userIcon = userIcon;
        this.phoneDisplayName = phoneDisplayName;
        this.phoneNumber = phoneNumber;
    }

    public Contact() {
    }

    public Bitmap getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Bitmap userIcon) {
        this.userIcon = userIcon;
    }

    public String getPhoneDisplayName() {
        return phoneDisplayName;
    }

    public void setPhoneDisplayName(String phoneDisplayName) {
        this.phoneDisplayName = phoneDisplayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
