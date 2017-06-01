package com.bsalazar.molonometro.general;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.GroupDetailActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import static java.security.AccessController.getContext;

/**
 * Created by bsalazar on 01/06/2017.
 */

public class ImageActivity extends AppCompatActivity {

    private final String NO_TITLE = "NO_TITLE";
    private final String NO_SUBTITLE = "NO_SUBTITLE";

    private Bitmap bmp;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail);

        getSupportActionBar().setHomeButtonEnabled(true);

        String imageBase64 = getIntent().getExtras().getString("image");
        int noImage = getIntent().getExtras().getInt("noImage", R.drawable.user_icon);

        String title = getIntent().getExtras().getString("title", NO_TITLE);
        String subtitle = getIntent().getExtras().getString("subtitle", NO_SUBTITLE);

        if (!title.equals(NO_TITLE))
            getSupportActionBar().setTitle(title);

        if (!subtitle.equals(NO_SUBTITLE))
            getSupportActionBar().setSubtitle(subtitle);


        ImageView image = (ImageView) findViewById(R.id.image);

        if (imageBase64 != null) {
            byte[] imageByteArray = Base64.decode(imageBase64, Base64.DEFAULT);
            bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            image.setImageBitmap(bmp);

        } else {
            image.setImageResource(noImage);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
//                if (type == 1) {
//                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
//                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(main);
//
//                } else
                    super.onBackPressed();
                return true;
            case R.id.action_share:
                shareBitmap(bmp, getTitle() + " " + new Date().toString());
                return true;
        }
        return true;
    }

    private void shareBitmap(Bitmap bitmap, String fileName) {
        try {
            File file = new File(getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
