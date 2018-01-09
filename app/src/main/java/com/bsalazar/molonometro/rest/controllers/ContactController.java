package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.widget.Toast;

import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.ContactsListJson;
import com.bsalazar.molonometro.rest.json.GetContactDetailJson;
import com.bsalazar.molonometro.rest.services.Parser;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.google.gson.Gson;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bsalazar on 01/03/2017.
 */

public class ContactController {

    public void getContactByID(final Context mContext, GetContactDetailJson contactsJson, final ServiceCallback callback) {

        Constants.restController.getService().getContactByID(contactsJson
                , new Callback<ContactJson>() {
                    @Override
                    public void success(ContactJson contactJson, Response response) {
                        callback.onSuccess(contactJson);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO obteniendo usuario\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                        callback.onFailure("");
                    }
                });
    }
}
