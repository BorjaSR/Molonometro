package com.bsalazar.molonometro.area_dashboard_group;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.Variables;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class GroupDetailActivity extends AppCompatActivity {

    private int highestScore = 1;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();
        setView();
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
        ImageView group_image = (ImageView) findViewById(R.id.group_image);

        String imageBase64 = Variables.Group.getImageBase64();
        if (imageBase64 != null){
            byte[] imageByteArray = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            group_image.setImageBitmap(bmp);

        } else {
            group_image.setImageDrawable(getResources().getDrawable(R.drawable.group_icon));
        }

        ctl.setTitle(Variables.Group.getName());
        ctl.setExpandedTitleColor(getResources().getColor(R.color.charcoal_gray));
        ctl.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_detail, menu);
        return true;
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
