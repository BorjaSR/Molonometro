package com.bsalazar.molonometro.area_register;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.BuildConfig;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.SendFileFTP;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.services.RestController;
import com.bsalazar.molonometro.rest.services.ServiceCallback;

import java.io.File;

public class SetFirstProfileDataActivity extends AppCompatActivity implements View.OnClickListener {

    final int GALERY_INPUT = 6;
    private final int CAMERA_INPUT = 2;

    ImageView profileImage;
    EditText user_name, user_name_real, user_state;
    Bitmap profileBitmap = null;
    private String mCurrentPhotoPath;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_first_profile_data_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;


        Constants.restController = new RestController();

        user_name = (EditText) findViewById(R.id.user_name);
        user_name_real = (EditText) findViewById(R.id.user_name_real);
        user_state = (EditText) findViewById(R.id.user_state);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
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
                final UpdateUserJson updateUserJson = new UpdateUserJson();
                updateUserJson.setEmail(Variables.User.getEmail());
                updateUserJson.setUserName(user_name.getText().toString());
                updateUserJson.setName(user_name_real.getText().toString());
                updateUserJson.setState(user_state.getText().toString());

                if(profileBitmap == null){
                    updateUser(updateUserJson);

                } else {
                    new SendFileFTP(String.valueOf(Variables.User.getUserID()), SendFileFTP.MODE_USER, profileBitmap, null).execute();

//                    new UserController().updateUserImage(this, profileBitmap, new ServiceCallback() {
//                        @Override
//                        public void onSuccess(String result) {
//                            updateUser(updateUserJson);
//                        }
//
//                        @Override
//                        public void onFailure(String result) {
//
//                        }
//                    });
                }

                break;

            case R.id.profileImage:
                showImageDialog();
                break;
        }
    }

    private void updateUser(UpdateUserJson updateUserJson){
        new UserController().updateUser(getApplicationContext(), updateUserJson, new ServiceCallback() {
            @Override
            public void onSuccess(String result) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String result) {

            }
        });
    }

    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        AlertDialog dialog = builder.create();
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
                    Snackbar.make(profileImage, "Sin permiso no se puede usar la camara.", Snackbar.LENGTH_SHORT).show();

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

    private Uri getOutputMediaFileUri()
    {
        //check for external storage
        if(isExternalStorageAvaiable())
        {
            File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//            File mediaStorageDir = getActivity().getFilesDir();
            File mediaFile;

            try {
                mediaFile = new File(mediaStorageDir.getPath() + "/temp.jpg");
                Log.i("st","File: "+Uri.fromFile(mediaFile));
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.i("St","Error creating file: " + mediaStorageDir.getAbsolutePath() + "/temp.jpg");
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
                    profileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    profileImage.setImageBitmap(profileBitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_INPUT) {
                profileBitmap = handleBigCameraPhoto();
                profileImage.setImageBitmap(profileBitmap);
            }
        }
    }


}
