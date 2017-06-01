package com.bsalazar.molonometro.area_new_group;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

/**
 * Created by bsalazar on 24/02/2017.
 */

public class FinishGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private final int GALERY_INPUT = 1;
    private final int CAMERA_INPUT = 2;

    private EditText group_name;
    private ImageView group_image;

    private Bitmap group_image_bitmap;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_group_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView participants_text = (TextView) findViewById(R.id.participants_text);
        group_image = (ImageView) findViewById(R.id.group_image);
        group_name = (EditText) findViewById(R.id.group_name);
        group_image_bitmap = null;

        participants_text.setText(getString(R.string.participants) + " (" + Variables.createGroupJson.getContacts().size() + ")");
        group_image.setOnClickListener(this);

        LinearLayout participants_container = (LinearLayout) findViewById(R.id.participants_container);
        for (Integer id : Variables.createGroupJson.getContacts()) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            final View participant = inflater.inflate(R.layout.contact_for_group_item, participants_container, false);

            ImageView contact_image = (ImageView) participant.findViewById(R.id.group_image);
            TextView contact_name = (TextView) participant.findViewById(R.id.participant_name);
            TextView contact_state = (TextView) participant.findViewById(R.id.participant_state);

            String image64 = "";
            String Name = "";
            String State = "";

            if (id == Variables.User.getUserID()) {
                image64 = Variables.User.getImageBase64();
                Name = getString(R.string.you);
                State = Variables.User.getState();

            } else {
                Contact contact = getContactByID(id);
                if (contact != null) {
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
                    Variables.createGroupJson.setGroupImage(null);

                    new GroupController().createGroup(this, Variables.createGroupJson, new ServiceCallbackInterface() {
                        @Override
                        public void onSuccess(String result) {

                            GroupJson groupJson = new Gson().fromJson(result, GroupJson.class);
                            if (groupJson.getFirebaseTopic() != null && !groupJson.getFirebaseTopic().equals(""))
                                FirebaseMessaging.getInstance().subscribeToTopic(groupJson.getFirebaseTopic());

                            if (group_image_bitmap != null) {
                                groupJson.setImage(Tools.encodeBitmapToBase64(group_image_bitmap));
                                new GroupController().updateGroupImage(getApplicationContext(), groupJson, new ServiceCallbackInterface() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(main);
                                    }

                                    @Override
                                    public void onFailure(String result) {

                                    }
                                });
                            } else {
                                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(main);
                            }
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
                showImageDialog();
                break;
        }
    }


    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.ask_image))
                .setPositiveButton(getString(R.string.camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dispatchTakePictureIntent();
                    }
                })
                .setNegativeButton(getString(R.string.galery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        openGalery();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f;

        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            mCurrentPhotoPath = null;
        }

        startActivityForResult(takePictureIntent, CAMERA_INPUT);
    }

    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "temp.jpeg";
        File albumF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageF = new File(albumF.getPath() + "/" + imageFileName);

        if (imageF.exists()) {
            imageF.delete();
        }
        Boolean res = imageF.createNewFile();
        return imageF;
    }

    private Bitmap handleBigCameraPhoto() {
        Bitmap bitmap = null;
        if (mCurrentPhotoPath != null) {
            bitmap = setPic();
            mCurrentPhotoPath = null;
        }
        return bitmap;
    }

    private Bitmap setPic() {

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 1;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
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

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_INPUT) {
                group_image_bitmap = handleBigCameraPhoto();
                group_image.setImageBitmap(group_image_bitmap);
            }
        }
    }

}
