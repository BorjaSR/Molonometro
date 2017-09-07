package com.bsalazar.molonometro.area_dashboard_group;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.BuildConfig;
import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_adjust.EditFieldActivity;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.json.AddUserToGroupJson;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class GroupDetailActivity extends AppCompatActivity {

    private final int GALERY_INPUT = 1;
    private final int CAMERA_INPUT = 2;
    LinearLayout participants_container;

    private ImageView group_image;
    private Activity activity;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }

        activity = this;

        setToolbar();
        setCollapsing();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void setCollapsing() {

        final CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.ctl);
        group_image = (ImageView) findViewById(R.id.group_image);

        final String imageBase64 = Variables.Group.getImageBase64();
        if (imageBase64 != null) {
            byte[] imageByteArray = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            group_image.setImageBitmap(bmp);

            Palette p = Palette.from(bmp).generate();
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(Tools.brightnessDown(p.getDominantSwatch().getRgb()));
                ctl.setContentScrimColor(p.getDominantSwatch().getRgb());
            }

        } else {
            group_image.setImageDrawable(getResources().getDrawable(R.drawable.group_icon));
        }

        group_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });

        ctl.setTitle(Variables.Group.getName());
        ctl.setExpandedTitleTextAppearance(R.style.CollapsingTextDetailStyle);
        ctl.setExpandedTitleColor(getResources().getColor(R.color.white));
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
            case R.id.action_edit:
                Intent intent = new Intent(this, EditFieldActivity.class);
                intent.putExtra(EditFieldActivity.MODE, EditFieldActivity.EDIT_GROUP_NAME);
                startActivity(intent);
                return true;
        }
        return true;
    }


    private void setView() {

        participants_container = (LinearLayout) findViewById(R.id.participants_container);
        participants_container.removeAllViews();

        if (userIsAdmin())
            setAddParticipantView();

        for (final Participant participant : Variables.Group.getParticipants()) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            final View participant_view = inflater.inflate(R.layout.participant_group_item, participants_container, false);
            participant_view.setTag(participant.getUserID());

            ImageView contact_image = (ImageView) participant_view.findViewById(R.id.group_image);
            TextView contact_name = (TextView) participant_view.findViewById(R.id.participant_name);
            TextView contact_state = (TextView) participant_view.findViewById(R.id.participant_state);
            LinearLayout admin_signal = (LinearLayout) participant_view.findViewById(R.id.admin_signal);

            String image64 = "";
            String Name = "";
            String State = "";

            if (participant.getUserID() == Variables.User.getUserID()) {
                image64 = Variables.User.getImageBase64();
                Name = getString(R.string.you);
                State = Variables.User.getState();

            } else {
                image64 = participant.getImageBase64();
                Name = participant.getName();
                State = participant.getState();


                participant_view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showParticipantOptionsDialog(participant);
                        return false;
                    }
                });
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

            if (participant.isAdmin())
                admin_signal.setVisibility(View.VISIBLE);
            else
                admin_signal.setVisibility(View.GONE);


            participants_container.addView(participant_view);
        }

        LinearLayout exit_group_button = (LinearLayout) findViewById(R.id.exit_group_button);
        exit_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogExitGroup();
            }
        });
    }

    public void showParticipantOptionsDialog(final Participant participant) {

        final ArrayList<String> items = new ArrayList<>();

        items.add("Ver a " + participant.getName());
        if (userIsAdmin()) {
            items.add("Eliminar a " + participant.getName());
            if (!participant.isAdmin())
                items.add("Hacer administrador de grupo");
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("[DIALOG RESPONSE]", dialog.toString() + " / " + which);
                        switch (which) {
                            case 0:
                                break;
                            case 1:
                                removeParticipant(participant);
                                break;
                            case 2:
                                makeUserAdmin(participant);
                                break;
                        }
                    }
                })
                .show();
    }

    private void removeParticipant(final Participant participant) {

        AddUserToGroupJson addUserToGroupJson = new AddUserToGroupJson();
        addUserToGroupJson.setUserID(Variables.User.getUserID());
        addUserToGroupJson.setGroupID(Variables.Group.getId());
        addUserToGroupJson.setContactID(participant.getUserID());

        new GroupController().removeUserFromGroup(addUserToGroupJson, new ServiceCallbackInterface() {
            @Override
            public void onSuccess(String result) {
                for (Participant participantGroup : Variables.Group.getParticipants())
                    if (participantGroup.getUserID() == participant.getUserID()) {
                        Variables.Group.getParticipants().remove(participantGroup);
                        break;
                    }

                for (int i = 0; i < participants_container.getChildCount(); i++)
                    if ((int) participants_container.getChildAt(i).getTag() == participant.getUserID())
                        participants_container.removeView(participants_container.getChildAt(i));

            }
        });
    }

    private void makeUserAdmin(final Participant participant) {

        final ProgressDialog progress = ProgressDialog.show(this, "",
                "Espere...", true);

        AddUserToGroupJson makeAdmin = new AddUserToGroupJson();
        makeAdmin.setContactID(participant.getUserID());
        makeAdmin.setGroupID(Variables.Group.getId());
        new GroupController().makeAdmin(makeAdmin, new ServiceCallbackInterface() {
            @Override
            public void onSuccess(String result) {
                progress.dismiss();
                for (int i = 0; i < participants_container.getChildCount(); i++)
                    if ((int) participants_container.getChildAt(i).getTag() == participant.getUserID())
                        participants_container.getChildAt(i).findViewById(R.id.admin_signal).setVisibility(View.VISIBLE);
                participant.setAdmin(true);
            }

            @Override
            public void onFailure(String result) {
                super.onFailure(result);
                progress.dismiss();
            }
        });
    }

    private void showDialogExitGroup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Variables.Group.getName())
                .setMessage(getString(R.string.wish_exit_group))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AddUserToGroupJson addUserToGroupJson = new AddUserToGroupJson();
                        addUserToGroupJson.setUserID(Variables.User.getUserID());
                        addUserToGroupJson.setGroupID(Variables.Group.getId());
                        addUserToGroupJson.setContactID(Variables.User.getUserID());

                        new GroupController().removeUserFromGroup(addUserToGroupJson, new ServiceCallbackInterface() {
                            @Override
                            public void onSuccess(String result) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(Variables.Group.getFirebaseTopic());

                                for (Participant participant : Variables.Group.getParticipants())
                                    if (participant.getUserID() == Variables.User.getUserID()) {
                                        Variables.Group.getParticipants().remove(participant);
                                        break;
                                    }
                                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(main);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.drawable.group_icon_green)
                .show();
    }

    private boolean userIsAdmin() {
        for (Participant participant : Variables.Group.getParticipants())
            if (participant.getUserID() == Variables.User.getUserID())
                return participant.isAdmin();
        return false;
    }

    private void setAddParticipantView() {

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View participant_view = inflater.inflate(R.layout.participant_group_item, participants_container, false);
        participant_view.setTag(-1);

        LinearLayout participant_group_container = (LinearLayout) participant_view.findViewById(R.id.participant_group_container);
        ImageView contact_image = (ImageView) participant_view.findViewById(R.id.group_image);
        TextView contact_name = (TextView) participant_view.findViewById(R.id.participant_name);
        TextView participant_state = (TextView) participant_view.findViewById(R.id.participant_state);

        contact_image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add_participant_icon));
        contact_name.setText(getString(R.string.add_participant));
        participant_state.setVisibility(View.GONE);

        participant_group_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddParticipantActivity.class);
                startActivity(intent);
            }
        });

        participants_container.addView(participant_view);
    }

    private void showImageDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.ask_image))
                .setPositiveButton(getString(R.string.camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            dispatchTakePictureIntent();
                        } else {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.PERMISSION_RESULT_WRITE_EXTERNAL_STORAGE);
                        }

                    }
                })
                .setNegativeButton(getString(R.string.galery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        openGalery();
                    }
                });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_RESULT_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else
                    Snackbar.make(group_image, "Sin permiso no se puede usar la camara.", Snackbar.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri uri = getOutputMediaFileUri();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        } catch (Exception e) {
            e.printStackTrace();
            mCurrentPhotoPath = null;
        }

        startActivityForResult(takePictureIntent, CAMERA_INPUT);
    }

    private Uri getOutputMediaFileUri() {
        //check for external storage
        if (isExternalStorageAvaiable()) {
            File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File mediaFile;

            try {
                mediaFile = new File(mediaStorageDir.getPath() + "/temp.jpg");
                Log.i("st", "File: " + Uri.fromFile(mediaFile));
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("St", "Error creating file: " + mediaStorageDir.getAbsolutePath() + "/temp.jpg");
                return null;
            }

            Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    mediaFile);

            mCurrentPhotoPath = mediaFile.getAbsolutePath();
            return uri;
        }
        //something went wrong
        return null;
    }

    private boolean isExternalStorageAvaiable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
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
                    final Bitmap new_image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    setGroupImage(new_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_INPUT) {
                setGroupImage(handleBigCameraPhoto());
            }
        }
    }

    public void setGroupImage(Bitmap bitmap) {
        GroupJson groupJson = new GroupJson();
        groupJson.setGroupID(Variables.Group.getId());
        final String new_image_64 = Tools.encodeBitmapToBase64(bitmap);
        groupJson.setImage(new_image_64);

        new GroupController().updateGroupImage(this, groupJson, new ServiceCallbackInterface() {
            @Override
            public void onSuccess(String result) {

                Variables.Group.setImageBase64(new_image_64);
                setCollapsing();
            }

            @Override
            public void onFailure(String result) {

                try {
                    byte[] imageByteArray = Base64.decode(Variables.Group.getImageBase64(), Base64.DEFAULT);

                    Glide.with(getApplicationContext())
                            .load(imageByteArray)
                            .asBitmap()
                            .dontAnimate()
                            .listener(new MyRequestListener(activity, group_image))
                            .into(group_image);

                } catch (Exception e) {
                    group_image.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
                }
            }
        });
    }
}
