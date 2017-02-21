package com.bsalazar.molonometro.area_register;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.services.RestController;

public class SetFirstProfileDataActivity extends AppCompatActivity implements View.OnClickListener {

    final int GALERY_INPUT = 6;

    ImageView profileImage;
    Bitmap profileBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_first_profile_data_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Constants.restController = new RestController();

        profileImage = (ImageView) findViewById(R.id.profileImage);
        profileImage.setOnClickListener(this);

        TextView user_name = (TextView) findViewById(R.id.user_name);
        user_name.setText(Variables.User.getName());

        FloatingActionButton next = (FloatingActionButton) findViewById(R.id.next);
        next.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.next:
                EditText user_state = (EditText) findViewById(R.id.user_state);
                UpdateUserJson updateUserJson = new UpdateUserJson();
                updateUserJson.setUserID(Variables.User.getUserID());
                updateUserJson.setName(Variables.User.getName());
                if(profileBitmap != null)
                    updateUserJson.setImage(Tools.encodeBitmapToBase64(profileBitmap));
                updateUserJson.setState(user_state.getText().toString());

                new UserController().updateUser(this, updateUserJson);
                break;

            case R.id.profileImage:
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
                    profileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    profileImage.setImageBitmap(Tools.getRoundedCroppedBitmap(profileBitmap));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
