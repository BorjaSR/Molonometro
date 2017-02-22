package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.rest.json.ContactsListJson;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by bsalazar and bangulo and Stackoverflow on 22/02/2017.
 */

public interface ServiceCallbackInterface {

    void onSuccess(String result);

    void onFailure(String result);
}



