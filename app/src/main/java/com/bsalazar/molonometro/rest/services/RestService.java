package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.ContactsListJson;
import com.bsalazar.molonometro.rest.json.CreateGroupJson;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.json.ResponseJson;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.json.UserIdJson;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by smesonero on 05/10/2015.
 */

public interface RestService {

    @POST("/user/createUser")
    void createUser(
            @Body CreateUserJson createUserJson,
            Callback<UserJson> callback);

    @POST("/user/updateUser")
    void updateUser(
            @Body UpdateUserJson updateUserJson,
            Callback<UserJson> callback);

    @POST("/user/updateUserImage")
    void updateUserImage(
            @Body UpdateUserJson updateUserJson,
            Callback<UserJson> callback);

    @POST("/user/checkContacts")
    void checkContacts(
            @Body ContactsListJson updateUserJson,
            Callback<List<ContactJson>> callback);



    @POST("/group/createGroup")
    void createGroup(
            @Body CreateGroupJson createGroupJson,
            Callback<CreateGroupJson> callback);

    @POST("/group/getGroupsByUser")
    void getGroupsByUser(
            @Body UserIdJson userIdJson,
            Callback<List<GroupJson>> callback);

}



