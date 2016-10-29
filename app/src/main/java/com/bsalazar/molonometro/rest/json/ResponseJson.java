package com.bsalazar.molonometro.rest.json;

/**
 * Created by Borja on 29/10/2016.
 */

public class ResponseJson {

    private String status;
    private Object body;

    public ResponseJson() {
    }

    public String getStatus() {
        return status;
    }

    public Object getBody() {
        return body;
    }
}
