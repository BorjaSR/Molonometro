package com.bsalazar.molonometro.rest.services;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Borja on 29/10/2016.
 */

public class RestClient {

//    private static final String URL_REST_SERVICES = "http://192.168.1.106:80/molonometro/v1"; //Localhost CASA
    private static final String URL_REST_SERVICES = "http://192.168.1.186:80/molonometro/v1"; //Localhost TRABAJO
//    private static final String URL_REST_SERVICES = "http://hostingtestbsalazar.esy.es/molonometro/v1"; //Servidor HOSTINGER

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
