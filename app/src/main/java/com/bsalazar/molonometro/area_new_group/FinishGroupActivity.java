package com.bsalazar.molonometro.area_new_group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_adjust.EditFieldActivity;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

/**
 * Created by bsalazar on 24/02/2017.
 */

public class FinishGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private final int GALERY_INPUT = 1;

    private EditText group_name;
    private ImageView group_image;

    private Bitmap group_image_bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_group_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView participants_text = (TextView) findViewById(R.id.participants_text);
        group_image = (ImageView) findViewById(R.id.group_image);
        group_name = (EditText) findViewById(R.id.group_name);

        participants_text.setText(getString(R.string.participants) + " (" + Variables.createGroupJson.getContacts().size() + ")");
        group_image.setOnClickListener(this);

        LinearLayout participants_container = (LinearLayout) findViewById(R.id.participants_container);
        for (Integer id : Variables.createGroupJson.getContacts()) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            final View participant = inflater.inflate(R.layout.contact_for_group_item, participants_container, false);

            ImageView contact_image = (ImageView) participant.findViewById(R.id.contact_image);
            TextView contact_name = (TextView) participant.findViewById(R.id.group_name_first_part);
            TextView contact_state = (TextView) participant.findViewById(R.id.item_detail);

            String image64 = "";
            String Name = "";
            String State = "";

            if (id == Variables.User.getUserID()) {
                image64 = Variables.User.getImageBase64();
                Name = getString(R.string.you);
                State = Variables.User.getState();

            } else {
                Contact contact = getContactByID(id);
                if (contact != null){
                    image64 = contact.getImageBase64();
                    Name = contact.getName();
                    State = contact.getState();
                }
            }

            try {
                Glide.with(this)
                        .load(Base64.decode(image64, Base64.DEFAULT))
                        .asBitmap()
                        .into(contact_image);

            } catch (Exception e) {
                contact_image.setImageResource(R.drawable.user_icon);
            }

            contact_name.setText(Name);
            contact_state.setText(State);

            participants_container.addView(participant);
        }

    }


    private Contact getContactByID(int id) {
        for (Contact contact : Variables.contactsWithApp)
            if (contact.getUserID() == id)
                return contact;
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finish_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_finish:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (group_name.getText().toString().length() != 0) {
                    imm.hideSoftInputFromWindow(group_name.getWindowToken(), 0);
                    Variables.createGroupJson.setGroupName(group_name.getText().toString());
                    Variables.createGroupJson.setGroupImage("");

                    new GroupController().createGroup(this, Variables.createGroupJson, new ServiceCallbackInterface() {
                        @Override
                        public void onSuccess(String result) {
                            finish();
                        }

                        @Override
                        public void onFailure(String result) {

                        }
                    });
                } else {
                    Snackbar.make(group_name, "Nombre del grupo obligatorio", Snackbar.LENGTH_SHORT).show();
                    group_name.requestFocus();
//                    imm.showSoftInput(group_name, 0);
                }
                break;
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_image:
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

                    group_image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    group_image.setImageBitmap(group_image_bitmap);
                    //TODO
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    group_image_bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
//
//                    group_image_bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
//
//                    Glide.with(getApplicationContext())
//                            .load(group_image_bitmap)
//                            .asBitmap()
//                            .dontAnimate()
//                            .into(group_image);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
