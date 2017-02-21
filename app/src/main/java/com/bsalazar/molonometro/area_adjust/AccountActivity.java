package com.bsalazar.molonometro.area_adjust;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
        profile_image.setImageBitmap(Tools.getRoundedCroppedBitmap(Variables.User.getImage()));
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

                    //compress the image
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    new_image.compress(Bitmap.CompressFormat.JPEG, 25, baos);

                    profile_image.setImageBitmap(Tools.getRoundedCroppedBitmap(new_image));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
