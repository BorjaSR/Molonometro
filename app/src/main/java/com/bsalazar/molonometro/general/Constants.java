package com.bsalazar.molonometro.general;

import android.app.FragmentManager;

import com.bsalazar.molonometro.rest.services.RestController;

/**
 * Created by bsalazar on 18/10/2016.
 */
public class Constants {


    public static final int NO_FRAG = -1;
    public static final int FRAG_ID_MAIN_SCREEN = 0;
    public static final int FRAG_ID_DASHBOARD_GROUP = 1;


    public static int HEIGHT_OF_TEMOMETER = 830;
    public static RestController restController;
    public static FragmentManager fragmentManager;

    public static String IMAGE_TRANSITION_NAME = "imageTransition";
}
