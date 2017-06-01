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
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.json.UserJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bsalazar on 21/02/2017.
 */

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private final int GALERY_INPUT = 1;
    private final int CAMERA_INPUT = 2;

    private ImageView profile_image;
    private TextView profile_user_name;
    private TextView profile_state;
    private TextView profile_phone;

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
        profile_phone = (TextView) findViewById(R.id.profile_phone);

        profile_image.setOnClickListener(this);
        profile_user_name.setOnClickListener(this);
        profile_state.setOnClickListener(this);

        try {
            byte[] imageByteArray = Base64.decode(Variables.User.getImageBase64(), Base64.DEFAULT);

            Glide.with(this)
                    .load(imageByteArray)
                    .asBitmap()
                    .dontAnimate()
                    .listener(new MyRequestListener(activity, profile_image))
                    .into(profile_image);

        } catch (Exception e) {
            profile_image.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillFields();
    }

    private void fillFields() {

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
                showImageDialog();
                break;
            case R.id.profile_user_name:
                Intent intent = new Intent(this, EditFieldActivity.class);
                intent.putExtra(EditFieldActivity.MODE, EditFieldActivity.EDIT_USER_NAME);
                startActivity(intent);
                break;
            case R.id.profile_state:
                Intent intent2 = new Intent(this, EditFieldActivity.class);
                intent2.putExtra(EditFieldActivity.MODE, EditFieldActivity.EDIT_USER_STATE);
                startActivity(intent2);
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
        File imageF = new File(albumF.getPath()+"/"+imageFileName);

        if (imageF.exists()){
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

    private void setImage(Bitmap bitmap){
        profile_image.setImageBitmap(null);

        new UserController().updateUserImage(this, bitmap, new ServiceCallbackInterface() {
            @Override
            public void onSuccess(String result) {

                byte[] bitmapdata = Base64.decode(Variables.User.getImageBase64(), Base64.DEFAULT);

                Glide.with(getApplicationContext())
                        .load(bitmapdata)
                        .asBitmap()
                        .dontAnimate()
                        .listener(new MyRequestListener(activity, profile_image))
                        .into(profile_image);
            }

            @Override
            public void onFailure(String result) {

                try {
                    byte[] imageByteArray = Base64.decode(Variables.User.getImageBase64(), Base64.DEFAULT);

                    Glide.with(getApplicationContext())
                            .load(imageByteArray)
                            .asBitmap()
                            .dontAnimate()
                            .listener(new MyRequestListener(activity, profile_image))
                            .into(profile_image);

                } catch (Exception e) {
                    profile_image.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
                }
            }
        });
    }
}
