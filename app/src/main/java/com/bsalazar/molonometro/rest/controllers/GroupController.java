package com.bsalazar.molonometro.rest.controllers;

import android.content.Context;
import android.widget.Toast;

import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.CreateGroupJson;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.json.UserIdJson;
import com.bsalazar.molonometro.rest.services.Parser;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Borja on 26/02/2017.
 */

public class GroupController {


    public void createGroup(final Context mContext, CreateGroupJson createGroupJson, final ServiceCallbackInterface callback) {

        Constants.restController.getService().createGroup(createGroupJson
                , new Callback<GroupJson>() {
                    @Override
                    public void success(GroupJson groupJson, Response response) {
                        Variables.createGroupJson = null;

                        callback.onSuccess(groupJson.getGroupID() + "");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO creando el grupo\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();

                        callback.onFailure("");
                    }
                });
    }

    public void updateGroup(final Context mContext, GroupJson updateGroupJson, final ServiceCallbackInterface callback) {

        Constants.restController.getService().updateGroup(updateGroupJson
                , new Callback<GroupJson>() {
                    @Override
                    public void success(GroupJson groupJson, Response response) {
                        callback.onSuccess("");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO updateGroup\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();

                        callback.onFailure("");
                    }
                });
    }

    public void updateGroupImage(final Context mContext, GroupJson updateGroupJson, final ServiceCallbackInterface callback) {

        Constants.restController.getService().updateGroupImage(updateGroupJson
                , new Callback<GroupJson>() {
                    @Override
                    public void success(GroupJson groupJson, Response response) {
                        callback.onSuccess("");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() != null)
                            Toast.makeText(mContext, "KO updateGroupImage\n" + error.getResponse().getStatus() + " " + error.getResponse().getReason(), Toast.LENGTH_SHORT).show();

                        callback.onFailure("");
                    }
                });
    }

    public void getGroupsByUser(final Context mContext, UserIdJson userIdJson, final ServiceCallbackInterface callback) {

        Constants.restController.getService().getGroupsByUser(userIdJson
                , new Callback<List<GroupJson>>() {
                    @Override
                    public void success(List<GroupJson> groupList, Response response) {
                        ArrayList<Group> groups = new ArrayList<>();
                        for (GroupJson groupJson : groupList)
                            groups.add(Parser.parseGroup(groupJson));

                        Variables.groups = groups;

                        callback.onSuccess("");
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        callback.onFailure("");
                    }
                });
    }

    public void getGroupByID(final Context mContext, GroupJson groupId, final ServiceCallbackInterface callback) {

        Constants.restController.getService().getGroupByID(groupId
                , new Callback<GroupJson>() {
                    @Override
                    public void success(GroupJson group, Response response) {
                        if (callback != null)
                            callback.onSuccess(new Gson().toJson(group));

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onFailure("");
                    }
                });
    }
    public void getGroupParticipantsByID(final Context mContext, int groupId, final ServiceCallbackInterface callback) {

        GroupJson groupJson = new GroupJson();
        groupJson.setGroupID(groupId);

        Constants.restController.getService().getGroupParticipantsByID(groupJson
                , new Callback<ArrayList<Participant>>() {
                    @Override
                    public void success(ArrayList<Participant> participants, Response response) {
                        Variables.Group.setParticipants(Parser.parseParticipants(participants));
                        if (callback != null)
                            callback.onSuccess(new Gson().toJson(participants));

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null)
                            callback.onFailure("");
                    }
                });
    }
}
