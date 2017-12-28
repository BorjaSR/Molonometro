package com.bsalazar.molonometro.area_adjust;

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
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.BuildConfig;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Memo;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.SendFileFTP;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * Created by bsalazar on 21/02/2017.
 */

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private final int GALERY_INPUT = 1;
    private final int CAMERA_INPUT = 2;

    private ImageView profile_image;
    private TextView notifText;
    private TextView profile_user_name;
    private TextView profile_state;
    private TextView profile_name;
    private TextView user_molopuntos;
    private TextView logout;

    private Activity activity;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjust_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity = this;

        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_user_name = (TextView) findViewById(R.id.profile_user_name);
        profile_state = (TextView) findViewById(R.id.profile_state);
        profile_name = (TextView) findViewById(R.id.profile_name);
        user_molopuntos = (TextView) findViewById(R.id.user_molopuntos);
        logout = (TextView) findViewById(R.id.logout);

        profile_image.setOnClickListener(this);
        profile_user_name.setOnClickListener(this);
        profile_name.setOnClickListener(this);
        profile_state.setOnClickListener(this);
        logout.setOnClickListener(this);

        new UserController().getUser(new ServiceCallback() {
            @Override
            public void onSuccess(String result) {
                fillFields();
                loadImage();
            }
        });
    }

    private void fillFields() {

        profile_user_name.setText(String.format(getString(R.string.user_name), Variables.User.getUserName()));
        profile_state.setText(Variables.User.getState());
        profile_name.setText(Tools.formatPhone(Variables.User.getName()));
        user_molopuntos.setText(Variables.User.getMolopuntos() + " Molopuntos");
    }

    private void loadImage() {
        try {

            Glide.with(this)
                    .load(Variables.User.getImageURL())
                    .asBitmap()
                    .dontAnimate()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(new MyRequestListener(activity, profile_image))
                    .into(profile_image);

        } catch (Exception e) {
            profile_image.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);

        return true;
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
                showImageDialog();
                break;
            case R.id.profile_user_name:
                Intent intent = new Intent(this, EditFieldActivity.class);
                intent.putExtra(EditFieldActivity.MODE, EditFieldActivity.EDIT_USER_NAME);
                startActivity(intent);
                break;
            case R.id.profile_name:
                Intent intent3 = new Intent(this, EditFieldActivity.class);
                intent3.putExtra(EditFieldActivity.MODE, EditFieldActivity.EDIT_NAME);
                startActivity(intent3);
                break;
            case R.id.profile_state:
                Intent intent2 = new Intent(this, EditFieldActivity.class);
                intent2.putExtra(EditFieldActivity.MODE, EditFieldActivity.EDIT_USER_STATE);
                startActivity(intent2);
                break;
            case R.id.logout:
                Memo.rememberMe(getApplicationContext(), Memo.NOT_SAVE);
                setResult(RESULT_OK, null);
                finish();
                break;
        }
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
                    Snackbar.make(profile_image, "Sin permiso no se puede usar la camara.", Snackbar.LENGTH_SHORT).show();

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
                    Bitmap new_image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    setImage(new_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_INPUT) {
                setImage(handleBigCameraPhoto());
            }
        }
    }

    private void setImage(Bitmap bitmap) {
        profile_image.setImageBitmap(null);

        new SendFileFTP(String.valueOf(Variables.User.getUserID()), SendFileFTP.MODE_USER, bitmap, new SendFileFTP.SendFileFTPListener() {
            @Override
            public void onFinish(boolean result, String URL) {
                if (result)
                    Glide.with(AccountActivity.this)
                            .load(Variables.User.getImageURL())
                            .asBitmap()
                            .dontAnimate()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .listener(new MyRequestListener(activity, profile_image))
                            .into(profile_image);
            }
        }).execute();

//        new UserController().updateUserImage(this, bitmap, new ServiceCallback() {
//            @Override
//            public void onSuccess(String result) {
//
//                byte[] bitmapdata = Base64.decode(Variables.User.getImageURL(), Base64.DEFAULT);
//
//                Glide.with(getApplicationContext())
//                        .load(bitmapdata)
//                        .asBitmap()
//                        .dontAnimate()
//                        .listener(new MyRequestListener(activity, profile_image))
//                        .into(profile_image);
//            }
//
//            @Override
//            public void onFailure(String result) {
//
//                try {
//                    byte[] imageByteArray = Base64.decode(Variables.User.getImageURL(), Base64.DEFAULT);
//
//                    Glide.with(getApplicationContext())
//                            .load(imageByteArray)
//                            .asBitmap()
//                            .dontAnimate()
//                            .listener(new MyRequestListener(activity, profile_image))
//                            .into(profile_image);
//
//                } catch (Exception e) {
//                    profile_image.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
//                }
//            }
//        });
    }
}
