package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.widget.Toast;

import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.services.Parser;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bsalazar on 23/03/2017.
 */

public class CommentsController {


    public void getCommentsByGroup(final Context mContext, int groupId, final ServiceCallbackInterface callback) {

        GroupJson groupJson = new GroupJson();
        groupJson.setGroupID(groupId);

        Constants.restController.getService().getCommentByGroup(groupJson
                , new Callback<ArrayList<Comment>>() {
                    @Override
                    public void success(ArrayList<Comment> commentsJson, Response response) {

                        Variables.Group.setComments(Parser.parseComments(commentsJson));

                        if (callback != null)
                            callback.onSuccess(new Gson().toJson(commentsJson));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onSuccess(error.getResponse().getReason());

                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO obteniendo comentarios\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getRepliesByComment(final Context mContext, int commentID, final ServiceCallbackInterface callback) {

        Comment commentJson = new Comment();
        commentJson.setCommentID(commentID);

        Constants.restController.getService().getRepliesByComment(commentJson
                , new Callback<ArrayList<Comment>>() {
                    @Override
                    public void success(ArrayList<Comment> commentsJson, Response response) {
                        if (callback != null)
                            callback.onSuccess(new Gson().toJson(commentsJson));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onSuccess(error.getResponse().getReason());

                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO obteniendo respuestas\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
