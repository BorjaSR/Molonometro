package com.bsalazar.molonometro.area_adjust.adapters;

import com.bsalazar.molonometro.entities.Contact;

/**
 * Created by bsalazar on 2/1/18.
 */

public interface RequestListener {

        void onRequestAccepted(Contact contact);

//        void onRequestSend(Contact contact);
//        void onRequestRejected(Contact contact);
}
