package com.bsalazar.molonometro.area_dashboard_group;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bumptech.glide.Glide;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class DashboardGroupActivityAUX extends AppCompatActivity {

    private int highestScore = 1;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_group_fragment_new);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();
        setView();

        getHighestScore();

        final RelativeLayout termometer_container = (RelativeLayout) findViewById(R.id.termometer_container);
        final RelativeLayout users_container = (RelativeLayout) findViewById(R.id.users_container);

        final float ter_height = termometer_container.getLayoutParams().height;

        //SET HEIGHT TERMOMETER
        Constants.HEIGHT_OF_TEMOMETER = (int) ((ter_height * 830) / 1050);

        users_container.removeAllViews();
        for (Participant user : Variables.Group.getParticipants()) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View user_termometer_view = inflater.inflate(R.layout.termometer_user, users_container, false);

            ImageView dashboard_user_image = (ImageView) user_termometer_view.findViewById(R.id.dashboard_user_image);
            TextView user_name = (TextView) user_termometer_view.findViewById(R.id.user_name);

            if (user.getUserID() == Variables.User.getUserID())
                user_name.setText(getString(R.string.you));
            else
                user_name.setText(Tools.cropNameSurname(user.getName()));

            try {
                byte[] imageByteArray = Base64.decode(user.getImageBase64(), Base64.DEFAULT);

                Glide.with(this)
                        .load(imageByteArray)
                        .asBitmap()
                        .listener(new MyRequestListener(this, dashboard_user_image))
                        .into(dashboard_user_image);

            } catch (Exception e) {
                dashboard_user_image.setImageResource(R.drawable.user_icon);
            }

            users_container.addView(user_termometer_view);
            users_container.getChildAt(users_container.getChildCount() - 1).setTranslationY(getPositionUser(user.getMolopuntos()));
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }
    private void setView() {

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.ctl);

        ctl.setTitle(Variables.Group.getName());
        ctl.setExpandedTitleColor(getResources().getColor(R.color.charcoal_gray));
        ctl.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
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
        for (Participant user : Variables.Group.getParticipants())
            if (user.getMolopuntos() > highestScore) highestScore = user.getMolopuntos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case android.R.id.title:
                Snackbar.make(findViewById(R.id.termometer_container), "Titulo pulsado", Snackbar.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }
}
