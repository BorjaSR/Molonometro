package com.bsalazar.molonometro.general;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Contact;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by bsalazar on 17/03/2017.
 */

public class MyRequestListener implements RequestListener {

    private View mView;
    private Context mContext;

    public MyRequestListener(Context context, View view) {
        mView = view;
        mContext = context;
    }

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {

        Animation up = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        up.setFillAfter(true);

        mView.startAnimation(up);
        return false;
    }
}
