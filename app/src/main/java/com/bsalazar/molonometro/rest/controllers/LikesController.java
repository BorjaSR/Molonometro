package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.widget.Toast;

import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.LikeJson;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.google.gson.Gson;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bsalazar on 23/03/2017.
 */

public class LikesController {

    public void addLikeToComment(final Context mContext, int commentID, final ServiceCallback callback) {

        LikeJson likeJson = new LikeJson();
        likeJson.setCommentID(commentID);
        likeJson.setUserID(Variables.User.getUserID());

        Constants.restController.getService().addLikeToComment(likeJson
                , new Callback<Boolean>() {
                    @Override
                    public void success(Boolean result, Response response) {
                        if (callback != null)
                            callback.onSuccess(new Gson().toJson(result));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onSuccess(error.getResponse().getReason());

                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO añadiendo like\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
