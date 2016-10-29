package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.widget.Toast;

import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.UserJson;
import com.bsalazar.molonometro.rest.services.Parser;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Borja on 29/10/2016.
 */

public class UserController {

    private String RESULT_OK = "200";

    public void createUser(final Context mContext, CreateUserJson createUserJson) {

        Constants.restController.getService().createUser(createUserJson
                , new Callback<UserJson>() {
                    @Override
                    public void success(UserJson userJson, Response response) {
                        Variables.User = Parser.parseUser(userJson);
                        Toast.makeText(mContext, "Exito creando al usuario " + Variables.User.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(mContext, "KO creando al usuario\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
