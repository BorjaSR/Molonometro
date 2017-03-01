package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.widget.Toast;

import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.ContactsListJson;
import com.bsalazar.molonometro.rest.services.Parser;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bsalazar on 01/03/2017.
 */

public class ContactController {

    public void checkContacts(final Context mContext, ContactsListJson contactsListJson, final ServiceCallbackInterface callback) {

        Constants.restController.getService().checkContacts(contactsListJson
                , new Callback<List<ContactJson>>() {
                    @Override
                    public void success(List<ContactJson> contactListJson, Response response) {

                        Variables.contacts.clear();
                        Variables.contactsWithApp.clear();

                        for (int i = 0; i < contactListJson.size(); i++) {
                            Variables.contacts.add(Parser.parseContact(contactListJson.get(i)));

                            if (contactListJson.get(i).isInApp())
                                Variables.contactsWithApp.add(Parser.parseContact(contactListJson.get(i)));
                        }

                        callback.onSuccess("");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO checkenado usuarios\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                        callback.onSuccess("");
                    }
                });
    }
}
