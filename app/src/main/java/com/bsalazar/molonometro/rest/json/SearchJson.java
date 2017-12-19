package com.bsalazar.molonometro.rest.json;

/**
 * Created by bsalazar on 19/12/17.
 */

public class SearchJson {

    private String Text;

    public SearchJson(String text) {
        Text = text;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
