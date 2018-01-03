package com.bsalazar.molonometro.rest.services;

import android.util.Log;

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

public abstract class ServiceCallback {

    public abstract void onSuccess(Object result);

    public void onFailure(String result) {
        Log.e("SERVICE ERROR", "");
    }
}



