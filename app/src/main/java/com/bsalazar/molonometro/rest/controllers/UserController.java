package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.bsalazar.molonometro.area_home.MainActivity;
import com.bsalazar.molonometro.area_register.RegisterActivity;
import com.bsalazar.molonometro.area_register.SetFirstProfileDataActivity;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Memo;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.ContactsListJson;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.json.UserJson;
import com.bsalazar.molonometro.rest.services.Parser;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.google.gson.Gson;

import java.util.List;

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
                        mContext.startActivity(new Intent(mContext, SetFirstProfileDataActivity.class));
                        ((RegisterActivity) mContext).finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO creando al usuario\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateUser(final Context mContext, UpdateUserJson updateUserJson, final ServiceCallbackInterface callback) {

        Constants.restController.getService().updateUser(updateUserJson
                , new Callback<UserJson>() {
                    @Override
                    public void success(UserJson userJson, Response response) {
                        Variables.User = Parser.parseUser(userJson);

                        Gson gson = new Gson();
                        String userStringJson = gson.toJson(Variables.User);
                        Memo.rememberMe(mContext, userStringJson);

                        if (Variables.User.getImageBase64() != null)
                            Variables.User.setImage(Tools.decodeBase64(Variables.User.getImageBase64()));


                        callback.onSuccess("Su puta madre ha ido bien, gracias ;)");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO actualizando al usuario\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();


                        callback.onFailure("");
                    }
                });

    }

    public void checkContacts(final Context mContext, ContactsListJson contactsListJson) {

        Constants.restController.getService().checkContacts(contactsListJson
                , new Callback<List<UserJson>>() {
                    @Override
                    public void success(List<UserJson> userJsonList, Response response) {
                        Variables.contacts2.clear();
                        Variables.contactsWithApp.clear();
                        for (int i = 0; i < userJsonList.size(); i++) {
                            Variables.contacts2.add(userJsonList.get(i));
                            if (userJsonList.get(i).isInApp())
                                Variables.contactsWithApp.add(userJsonList.get(i));
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO checkenado usuarios\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateUserImage(Context context, Bitmap image_profile, ServiceCallbackInterface callback) {
        UpdateUserJson updateUserJson = getUpdateUserJson();
        updateUserJson.setImage(Tools.encodeBitmapToBase64(image_profile));

        updateUser(context, updateUserJson, callback);
    }

    private UpdateUserJson getUpdateUserJson() {
        UpdateUserJson userJson = new UpdateUserJson();
        userJson.setImage(Variables.User.getImageBase64());
        userJson.setName(Variables.User.getName());
        userJson.setState(Variables.User.getState());
        userJson.setUserID(Variables.User.getUserID());
        return userJson;
    }
}
