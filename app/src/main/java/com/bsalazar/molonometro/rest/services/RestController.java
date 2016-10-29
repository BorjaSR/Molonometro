package com.bsalazar.molonometro.rest.services;

import android.content.Context;
import android.widget.Toast;

import com.bsalazar.molonometro.rest.json.ResponseJson;

import retrofit.RetrofitError;

public class RestController {

    /**
     * ATTRIBUTES
     **/
    // Context
    public RestController() {
        restClient = new RestClient();
    }

    private Context fatherActivity;
    private RestClient restClient;

    public RestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public RestController(Context fatherActivity) {
        this.fatherActivity = fatherActivity;
        restClient = new RestClient();
    }

    /**
     * GETTERS & SETTERS
     **/

    public RestService getService() {
        return restClient.getRestService();
    }

    /**
     * EXTRA METHODS
     **/
    // (for Parsers and others)

//    public static void showError(Context fatherActivity, RetrofitError error) {
//        // Get Status Code
//        int code;
//        if (error != null && error.getResponse() != null){
//            code = error.getResponse().getStatus();
//
//            // Show error by Status Code
//            switch (code) {
//                default:
//                    Toast.makeText(fatherActivity, "Server Error: " + code, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    public static void showError(Context fatherActivity, ResponseJson responseJson) {
        // Get Status Code
        int code = Integer.parseInt(responseJson.getStatus());
        String body = (String) responseJson.getBody();

        // Show error by Status Code
        switch (code) {
            case 431:
                Toast.makeText(fatherActivity, "Ya existe un usuario con este telefono", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(fatherActivity, "Server Error: " + code, Toast.LENGTH_SHORT).show();
        }
    }
}
