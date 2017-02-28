package com.bsalazar.molonometro.area_dashboard_group;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.UserGroup;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;

import java.util.ArrayList;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class DashboardGroupActivity extends AppCompatActivity {

    private ArrayList<UserGroup> users;
    private int highestScore = 0;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_group_fragment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        users = new ArrayList<>();
        users.add(new UserGroup("Sergio DID", 130));
        users.add(new UserGroup("Borja 2.0 DID", 75));
        users.add(new UserGroup("Tu", 15));
        users.add(new UserGroup("Juan DID", 90));
        users.add(new UserGroup("David DID", 66));

        getHighestScore();

        final RelativeLayout termometer_container = (RelativeLayout) findViewById(R.id.termometer_container);
        final RelativeLayout users_container = (RelativeLayout) findViewById(R.id.users_container);
        final LinearLayout shadow = (LinearLayout) findViewById(R.id.shadow);
        ScrollView scroll_dashboard_group = (ScrollView) findViewById(R.id.scroll_dashboard_group);
        final SeekBar seek_bar = (SeekBar) findViewById(R.id.seek_bar);

        final float ter_height = termometer_container.getLayoutParams().height;

        //SET HEIGHT TERMOMETER
        Constants.HEIGHT_OF_TEMOMETER = (int) ((ter_height * 830) / 1050);

        scroll_dashboard_group.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                termometer_container.setTranslationY(scrollY / -2);
                float alpha = (float) scrollY / ter_height;
                if (alpha <= 1)
                    shadow.setAlpha((float) scrollY / ter_height);

            }
        });

        users_container.removeAllViews();
        for (UserGroup user : users) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View user_termometer_view = inflater.inflate(R.layout.termometer_user, users_container, false);

            TextView user_name = (TextView) user_termometer_view.findViewById(R.id.user_name);
            user_name.setText(user.getName());

            users_container.addView(user_termometer_view);
            users_container.getChildAt(users_container.getChildCount() - 1).setTranslationY(getPositionUser(user.getMolopuntos()));
        }

        seek_bar.setMax(Constants.HEIGHT_OF_TEMOMETER);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                users_container.getChildAt(2).setTranslationY(Constants.HEIGHT_OF_TEMOMETER - i);
                Log.d("[SEEKBAR INFO]", i + "");

                if(i > seek_bar.getMax()/2){
                    enterReveal();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void enterReveal() {
        // previously invisible view
        final View myView = findViewById(R.id.text);

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }

    private int getPositionUser(int molopuntos) {
        int relative_position = (molopuntos * Constants.HEIGHT_OF_TEMOMETER) / highestScore;
        return Constants.HEIGHT_OF_TEMOMETER - relative_position;
    }

    public void getHighestScore() {
        for (UserGroup user : users)
            if (user.getMolopuntos() > highestScore) highestScore = user.getMolopuntos();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return true;
    }
}
