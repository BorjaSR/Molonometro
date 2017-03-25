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

                        ArrayList<Comment> comments = Parser.parseReply(commentsJson);

                        if (callback != null)
                            callback.onSuccess(new Gson().toJson(comments));
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


    public void addCommentToGroup(final Context mContext, Comment comment, final ServiceCallbackInterface callback) {

        Constants.restController.getService().addCommentToGroup(comment
                , new Callback<Boolean>() {
                    @Override
                    public void success(Boolean result, Response response) {
                        if (callback != null)
                            callback.onSuccess(result.toString());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onSuccess(error.getResponse().getReason());

                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO enviando comentario\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addReplyToComment(final Context mContext, Comment comment, final ServiceCallbackInterface callback) {

        Constants.restController.getService().addReplyToComment(comment
                , new Callback<Boolean>() {
                    @Override
                    public void success(Boolean result, Response response) {
                        if (callback != null)
                            callback.onSuccess(result.toString());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onSuccess(error.getResponse().getReason());

                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO enviando respuesta\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
