package com.bsalazar.molonometro.general;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.transition.Fade;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.bsalazar.molonometro.R;

/**
 * Created by bsalazar on 23/05/2017.
 */

public class PhotoDetailActivity extends Activity {

    private ImageView group_image_dialog;
    private Activity activity;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail_dialog_fragment);
        activity = this;

        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());

        group_image_dialog = (ImageView) findViewById(R.id.group_image_dialog);

//        final String imageURL = getIntent().getExtras().getString("image");
//        final int noImage = getIntent().getExtras().getInt("noImage");
        final String title = getIntent().getExtras().getString("title", getString(R.string.image));

        final byte[] byteArray = getIntent().getByteArrayExtra("imageBytes");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        group_image_dialog.setImageBitmap(bmp);

        group_image_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putInt("type", 1);
//                args.putString("image", imageURL);
//                args.putInt("noImage", noImage);
                args.putString("title", title);

                Intent intent = new Intent(activity, ImageActivity.class);
                intent.putExtras(args);
                intent.putExtra("imageBytes", byteArray);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(activity, group_image_dialog, getString(R.string.image_transition));
                    startActivity(intent, options.toBundle());
                } else
                    startActivity(intent);

                finish();
            }
        });

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void enterReveal(View myView) {
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
}
