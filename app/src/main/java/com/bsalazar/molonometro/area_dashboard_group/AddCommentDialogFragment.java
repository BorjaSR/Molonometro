package com.bsalazar.molonometro.area_dashboard_group;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bsalazar.molonometro.BuildConfig;
import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.adapters.AutoCompleteAdapter;
import com.bsalazar.molonometro.area_dashboard_group.adapters.RepliesRecyclerAdapter;
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.entities.LastEvent;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.CommentsController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bsalazar on 10/05/2017.
 */

public class AddCommentDialogFragment extends DialogFragment  implements View.OnClickListener {

    private final int GALERY_INPUT = 1;
    private final int CAMERA_INPUT = 2;

    private Context mContext;

    private AutoCompleteTextView destinationUser;
    private EditText comment_text;
    private ImageView comment_image;
    private ImageView comment_camera;
    private TextView send_button;

    private boolean isUserInGroup = false;
    private Participant participantToSend;
    private ArrayList<Participant> participants_without_you;
    private AutoCompleteAdapter autoCompleteAdapter;
    private String mCurrentPhotoPath;
    private Bitmap comment_bitmap;

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.AddCommentsDialogAnimation;

        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_comments_dialog_fragment, container,
                false);

        mContext = getActivity().getApplicationContext();

        destinationUser = (AutoCompleteTextView) rootView.findViewById(R.id.destinationUser);
        comment_text = (EditText) rootView.findViewById(R.id.comment_text);
        comment_image = (ImageView) rootView.findViewById(R.id.comment_image);
        comment_camera = (ImageView) rootView.findViewById(R.id.comment_camera);
        send_button = (TextView) rootView.findViewById(R.id.send_button);

        comment_camera.setOnClickListener(this);
        send_button.setOnClickListener(this);


        participants_without_you = new ArrayList<>();
        for (Participant user : Variables.Group.getParticipants())
            if (user.getUserID() != Variables.User.getUserID())
                participants_without_you.add(user);

        autoCompleteAdapter = new AutoCompleteAdapter(getActivity().getApplicationContext(), R.layout.autocomplete_participant_item, participants_without_you);
        destinationUser.setAdapter(autoCompleteAdapter);
        destinationUser.setThreshold(1);


        destinationUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                participantToSend = autoCompleteAdapter.getItem(i);
                comment_text.requestFocus();
            }
        });


        destinationUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userIsInGroup(destinationUser.getText().toString())) {
                    destinationUser.setTextColor(ContextCompat.getColor(mContext, R.color.charcoal_gray));
                    isUserInGroup = true;
                } else {
                    destinationUser.setTextColor(ContextCompat.getColor(mContext, R.color.wrong_color));
                    isUserInGroup = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.send_button:

                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                if (isUserInGroup) {
                    if (comment_text.getText().toString().length() != 0) {
                        final Comment comment = new Comment();
                        comment.setGroupID(Variables.Group.getId());
                        comment.setUserID(Variables.User.getUserID());
                        comment.setDestinationUserID(participantToSend.getUserID());
                        comment.setText(comment_text.getText().toString());
                        if (comment_bitmap == null)
                            comment.setImage("");
                        else
                            comment.setImage(Tools.encodeBitmapToBase64(comment_bitmap));

                        final ProgressDialog progress = ProgressDialog.show(getActivity(), "",
                                "Enviando...", true);

                        new CommentsController().addCommentToGroup(mContext, comment, new ServiceCallbackInterface() {
                            @Override
                            public void onSuccess(String result) {
                                progress.dismiss();

                                comment.setUserName(Variables.User.getName());
                                comment.setUserImage(Variables.User.getImageBase64());
                                comment.setDestinationUserName(participantToSend.getName());
                                comment.setDate(new Date());

                                comment.setComments(new ArrayList<Integer>());
                                comment.setLikes(new ArrayList<Integer>());

                                ((DashboardGroupActivity) getActivity()).addComment(comment);
                                dismiss();
                            }

                            @Override
                            public void onFailure(String result) {
                                progress.dismiss();
                            }
                        });

                    } else
                        Toast.makeText(mContext, getString(R.string.not_send_empty), Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(mContext, getString(R.string.vote_someone_real), Toast.LENGTH_SHORT).show();

                break;
            case R.id.comment_camera:
                showImageDialog();
                break;
        }
    }

    private Boolean userIsInGroup(String name) {
        for (Participant participant : participants_without_you)
            if (participant.getName().equals(name))
                return true;
        return false;
    }

    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.ask_image))
                .setPositiveButton(getString(R.string.camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            dispatchTakePictureIntent();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
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
                    Snackbar.make(comment_text, "Sin permiso no se puede usar la camara.", Snackbar.LENGTH_SHORT).show();

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
            File mediaStorageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

            Uri uri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
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
                    comment_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    comment_image.setVisibility(View.VISIBLE);
                    comment_image.setImageBitmap(comment_bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_INPUT) {
                comment_bitmap = handleBigCameraPhoto();
                comment_image.setVisibility(View.VISIBLE);
                comment_image.setImageBitmap(comment_bitmap);
            }
        }
    }

    public void setImage(Bitmap bitmap){
        comment_image.setVisibility(View.VISIBLE);
        comment_image.setImageBitmap(bitmap);
    }
}
