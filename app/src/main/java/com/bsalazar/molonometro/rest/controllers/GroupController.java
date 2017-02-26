package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.widget.Toast;

import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.CreateGroupJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Borja on 26/02/2017.
 */

public class GroupController {


    public void createGroup(final Context mContext, CreateGroupJson createGroupJson, final ServiceCallbackInterface callback) {

        Constants.restController.getService().createGroup(createGroupJson
                , new Callback<CreateGroupJson>() {
                    @Override
                    public void success(CreateGroupJson userJson, Response response) {
                        Toast.makeText(mContext, "OK !!", Toast.LENGTH_SHORT).show();
                        Variables.createGroupJson = null;

                        callback.onSuccess("");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO creando el grupo\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();

                        callback.onFailure("");
                    }
                });
    }
}
