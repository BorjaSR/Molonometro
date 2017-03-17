package com.bsalazar.molonometro.firebase;

import android.content.Context;
import android.util.Log;

import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by bsalazar on 17/03/2017.
 */

public class SetFirebaseTokenThread extends Thread {

    private Context mContext;

    public SetFirebaseTokenThread(Context context) {
        this.mContext = context;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                final String firebaseToken = FirebaseInstanceId.getInstance().getToken();
                Log.d("[FIREBASE TOKEN] ", firebaseToken);
                if (firebaseToken != null) {
                    if (Variables.User != null) {
                        new UserController().updateFirebaseToken(mContext, firebaseToken, new ServiceCallbackInterface() {
                            @Override
                            public void onSuccess(String result) {
                                Variables.User.setFirebaseToken(firebaseToken);
                                Variables.FirebaseToken = firebaseToken;
                            }

                            @Override
                            public void onFailure(String result) {

                            }

                        });
                    }
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
