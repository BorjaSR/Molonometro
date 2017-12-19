package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Memo;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.FriendRequestJson;
import com.bsalazar.molonometro.rest.json.RequestFriendJson;
import com.bsalazar.molonometro.rest.json.SearchJson;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.json.UserJson;
import com.bsalazar.molonometro.rest.services.Parser;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Borja on 29/10/2016.
 */

public class UserController {

    public void login(UserJson userJson, final ServiceCallback callback) {

        Constants.restController.getService().login(userJson
                , new Callback<Integer>() {
                    @Override
                    public void success(Integer userID, Response response) {
                        if (userID != -1 && callback != null)
                            callback.onSuccess(userID.toString());
                        else if (callback != null)
                            callback.onFailure("KO");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onFailure("KO");
                    }
                });
    }

    public void createUser(CreateUserJson createUserJson, final ServiceCallback callback) {

        Constants.restController.getService().createUser(createUserJson
                , new Callback<UserJson>() {
                    @Override
                    public void success(UserJson userJson, Response response) {
                        if (userJson.getEmail() != null) {
                            Variables.User = Parser.parseUser(userJson);

                            if (callback != null)
                                callback.onSuccess("");
                        } else if (callback != null)
                            callback.onFailure("");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null && callback != null)
                            callback.onFailure("KO");
                    }
                });
    }

    public void updateUserName(final Context mContext, String userName, final ServiceCallback callback) {
        UpdateUserJson updateUserJson = getUpdateUserJson();
        updateUserJson.setImage(null);
        updateUserJson.setUserName(userName);
        updateUser(mContext, updateUserJson, callback);
    }

    public void updateName(final Context mContext, String name, final ServiceCallback callback) {
        UpdateUserJson updateUserJson = getUpdateUserJson();
        updateUserJson.setImage(null);
        updateUserJson.setName(name);
        updateUser(mContext, updateUserJson, callback);
    }

    public void updateUserState(final Context mContext, String state, final ServiceCallback callback) {
        UpdateUserJson updateUserJson = getUpdateUserJson();
        updateUserJson.setImage(null);
        updateUserJson.setState(state);
        updateUser(mContext, updateUserJson, callback);
    }

    public void updateUser(final Context mContext, UpdateUserJson updateUserJson, final ServiceCallback callback) {
        updateUserJson.setUserID(Variables.User.getUserID());
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


    public void updateUserImage(final Context mContext, Bitmap image_profile, final ServiceCallback callback) {

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

    public void updateFirebaseToken(final Context mContext, String firebaseToken, final ServiceCallback callback) {

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
        userJson.setUserName(Variables.User.getUserName());
        userJson.setEmail(Variables.User.getEmail());
        userJson.setName(Variables.User.getName());
        userJson.setState(Variables.User.getState());
        userJson.setUserID(Variables.User.getUserID());
        return userJson;
    }

    public void getUser(final ServiceCallback callback) {

        UpdateUserJson updateUserJson = new UpdateUserJson();
        updateUserJson.setUserID(Variables.User.getUserID());
        Constants.restController.getService().getUser(updateUserJson
                , new Callback<UserJson>() {
                    @Override
                    public void success(UserJson userJson, Response response) {
                        try {

                            Variables.User = Parser.parseUser(userJson);

                            if (Variables.User.getImageBase64() != null)
                                Variables.User.setImage(Tools.decodeBase64(Variables.User.getImageBase64()));

                            Variables.User.setMolopuntos(userJson.getMolopuntos());

                            if (callback != null)
                                callback.onSuccess("");

                        } catch (Exception e) {
                            e.printStackTrace();
                            if (callback != null)
                                callback.onFailure("");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onFailure("");
                    }
                });

    }

    public void searchUsers(String text, final ServiceCallback callback) {

        Constants.restController.getService().searchUsers(new SearchJson(text)
                , new Callback<List<ContactJson>>() {
                    @Override
                    public void success(List<ContactJson> userJsonList, Response response) {
                        ArrayList<Contact> contacts = new ArrayList<>();
                        for (ContactJson contactJson : userJsonList)
                            if (contactJson.getUserID() != Variables.User.getUserID())
                                contacts.add(Parser.parseContact(contactJson));

                        if (callback != null)
                            callback.onSuccess(new Gson().toJson(contacts));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onFailure("");
                    }
                });

    }

    public void requestFriendship(RequestFriendJson requestFriendJson, final ServiceCallback callback) {

        Constants.restController.getService().requestFriendship(requestFriendJson
                , new Callback<Boolean>() {
                    @Override
                    public void success(Boolean result, Response response) {
                        if (callback != null && result)
                            callback.onSuccess("true");
                        else if (callback != null)
                            callback.onFailure("false");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onFailure("false");
                    }
                });

    }

    public void getRequest(UserJson userJson, final ServiceCallback callback) {

        Constants.restController.getService().getRequest(userJson
                , new Callback<List<FriendRequestJson>>() {
                    @Override
                    public void success(List<FriendRequestJson> result, Response response) {
                        if (callback != null)
                            callback.onSuccess("true");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onFailure("false");
                    }
                });

    }
}
