package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.ResponseJson;
import com.bsalazar.molonometro.rest.json.UserJson;

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

}



