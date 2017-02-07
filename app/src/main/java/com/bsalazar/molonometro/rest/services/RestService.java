package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.rest.json.ContactsListJson;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.ResponseJson;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
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
    public void createUser(
            @Body CreateUserJson createUserJson,
            Callback<UserJson> callback);

    @POST("/user/updateUser")
    public void updateUser(
            @Body UpdateUserJson updateUserJson,
            Callback<UserJson> callback);

    @POST("/user/checkContacts")
    public void checkContacts(
            @Body ContactsListJson updateUserJson,
            Callback<List<UserJson>> callback);

}



