package com.bsalazar.molonometro.entities;

import android.graphics.Bitmap;

/**
 * Created by bsalazar on 18/10/2016.
 */
public class PhoneContact {

    private String phoneDisplayName;
    private String phoneNumber;

    public PhoneContact(String phoneDisplayName, String phoneNumber) {
        this.phoneDisplayName = phoneDisplayName;
        this.phoneNumber = phoneNumber;
    }

    public PhoneContact() {
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
