package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.bsalazar.molonometro.area_register.RegisterActivity;
import com.bsalazar.molonometro.area_register.SetFirstProfileDataActivity;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Memo;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.ContactJson;
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

    public void updateUserName(final Context mContext, String name, final ServiceCallbackInterface callback) {
        UpdateUserJson updateUserJson = getUpdateUserJson();
        updateUserJson.setImage(null);
        updateUserJson.setName(name);
        updateUser(mContext, updateUserJson, callback);
    }

    public void updateUserState(final Context mContext, String state, final ServiceCallbackInterface callback) {
        UpdateUserJson updateUserJson = getUpdateUserJson();
        updateUserJson.setImage(null);
        updateUserJson.setState(state);
        updateUser(mContext, updateUserJson, callback);
    }

    private void updateUser(final Context mContext, UpdateUserJson updateUserJson, final ServiceCallbackInterface callback) {
        updateUserJson.setImage(null);

        Constants.restController.getService().updateUser(updateUserJson
                , new Callback<UserJson>() {
                    @Override
                    public void success(UserJson userJson, Response response) {

                        String user_image = Variables.User.getImageBase64();
                        Variables.User = Parser.parseUser(userJson);
                        Variables.User.setImageBase64(user_image);

                        Gson gson = new Gson();
                        String userStringJson = gson.toJson(Variables.User);
                        Memo.rememberMe(mContext, userStringJson);

                        if (Variables.User.getImageBase64() != null)
                            Variables.User.setImage(Tools.decodeBase64(Variables.User.getImageBase64()));

                        callback.onSuccess("");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO actualizando al usuario\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();

                        callback.onFailure("");
                    }
                });

    }


    public void updateUserImage(final Context mContext, Bitmap image_profile, final ServiceCallbackInterface callback) {

        UpdateUserJson updateUserJson = getUpdateUserJson();
        updateUserJson.setImage(Tools.encodeBitmapToBase64(image_profile));

        Constants.restController.getService().updateUserImage(updateUserJson
                , new Callback<UserJson>() {
                    @Override
                    public void success(UserJson userJson, Response response) {
                        Variables.User = Parser.parseUser(userJson);

                        //Guardar usuario en local
                        Gson gson = new Gson();
                        String userStringJson = gson.toJson(Variables.User);
                        Memo.rememberMe(mContext, userStringJson);

                        if (Variables.User.getImageBase64() != null)
                            Variables.User.setImage(Tools.decodeBase64(Variables.User.getImageBase64()));


                        callback.onSuccess("");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO actualizando Imagen de usuario\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();


                        callback.onFailure("");
                    }
                });

    }

    public void updateFirebaseToken(final Context mContext, String firebaseToken, final ServiceCallbackInterface callback) {

        UpdateUserJson updateUserJson = new UpdateUserJson();
        updateUserJson.setUserID(Variables.User.getUserID());
        updateUserJson.setFirebaseToken(firebaseToken);

        Constants.restController.getService().updateFirebaseToken(updateUserJson
                , new Callback<Boolean>() {
                    @Override
                    public void success(Boolean userJson, Response response) {

                        if (callback != null)
                            callback.onSuccess("");
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        if (callback != null)
                            callback.onFailure("");
                    }
                });

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
