package com.bsalazar.molonometro.area_adjust;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.json.UserJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bsalazar on 21/02/2017.
 */

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private final int GALERY_INPUT = 1;

    private ImageView profile_image;
    private TextView profile_user_name;
    private TextView profile_state;
    private TextView profile_phone;

    private Bitmap new_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjust_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_user_name = (TextView) findViewById(R.id.profile_user_name);
        profile_state = (TextView) findViewById(R.id.profile_state);
        profile_phone = (TextView) findViewById(R.id.profile_phone);

        fillFields();

        profile_image.setOnClickListener(this);

    }

    private void fillFields() {

        byte[] imageByteArray = Base64.decode(Variables.User.getImageBase64(), Base64.DEFAULT);

        Glide.with(this)
                .load(imageByteArray)
                .asBitmap()
                .dontAnimate()
                .into(profile_image);

//        profile_image.setImageBitmap(Variables.User.getImage());

        profile_user_name.setText(Variables.User.getName());
        profile_state.setText(Variables.User.getState());
        profile_phone.setText(Tools.formatPhone(Variables.User.getPhone()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_image:
                openGalery();
                break;
        }
    }

    public void openGalery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALERY_INPUT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALERY_INPUT) {
                try {

                    new_image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    profile_image.setImageBitmap(null);
                    
                    new UserController().updateUserImage(this, new_image, new ServiceCallbackInterface() {
                        @Override
                        public void onSuccess(String result) {

                            //compress the image
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            new_image.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                            byte[] bitmapdata = baos.toByteArray();

                            Glide.with(getApplicationContext())
                                    .load(bitmapdata)
                                    .asBitmap()
                                    .dontAnimate()
                                    .into(profile_image);
                        }

                        @Override
                        public void onFailure(String result) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
