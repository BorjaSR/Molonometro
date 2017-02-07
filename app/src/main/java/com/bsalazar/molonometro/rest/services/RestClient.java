package com.bsalazar.molonometro.rest.services;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by smesonero on 05/10/2015.
 */

public class RestClient {

    private static final String URL_REST_SERVICES = "http://192.168.1.104:80/molonometro/v1"; //Localhost

    private RestService restService;

    public RestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(URL_REST_SERVICES)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        restService = restAdapter.create(RestService.class);
    }

    public RestService getRestService() {
        return restService;
    }
}
