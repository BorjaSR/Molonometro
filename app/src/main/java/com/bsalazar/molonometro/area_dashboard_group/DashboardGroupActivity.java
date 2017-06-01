package com.bsalazar.molonometro.area_dashboard_group;

import android.Manifest;
import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.adapters.AutoCompleteAdapter;
import com.bsalazar.molonometro.area_dashboard_group.adapters.CommentsRecyclerAdapter;
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.entities.LastEvent;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.CommentsController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class DashboardGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private final int GALERY_INPUT = 1;
    private final int CAMERA_INPUT = 2;

    private float ter_height;
    private int highestScore = 1;
    private RelativeLayout termometer_container;
    private RelativeLayout users_container;
    private LinearLayout shadow;
    private RecyclerView commentsRecyclerView;
    private CommentsRecyclerAdapter adapter;

    private FloatingActionButton fab;
    private LinearLayout add_comment_container;
    private AutoCompleteTextView destinationUser;
    private TextView send_button, no_comments;
    private EditText comment_text;
    private ImageView comment_camera;
    private ImageView comment_image;
    private ProgressBar loading_comments;

    private AutoCompleteAdapter autoCompleteAdapter;
    private boolean isAddCommentShowed = false;
    private boolean isUserInGroup = false;
    private Participant participantToSend;
    private Bitmap comment_bitmap;

    private int ScrollY = 0;

    private Activity activity;
    private String mCurrentPhotoPath;

    ArrayList<Participant> participants_without_you;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_group_fragment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.activity = this;

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(Variables.Group.getName());
        }

        getHighestScore();


        termometer_container = (RelativeLayout) findViewById(R.id.termometer_container);
        users_container = (RelativeLayout) findViewById(R.id.users_container);
        shadow = (LinearLayout) findViewById(R.id.shadow);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        add_comment_container = (LinearLayout) findViewById(R.id.add_comment_container);
        destinationUser = (AutoCompleteTextView) findViewById(R.id.destinationUser);
        send_button = (TextView) findViewById(R.id.send_button);
        no_comments = (TextView) findViewById(R.id.no_comments);
        comment_text = (EditText) findViewById(R.id.comment_text);
        comment_camera = (ImageView) findViewById(R.id.comment_camera);
        comment_image = (ImageView) findViewById(R.id.comment_image);
        loading_comments = (ProgressBar) findViewById(R.id.loading_comments);

        participants_without_you = new ArrayList<>();

        fab.setOnClickListener(this);
        send_button.setOnClickListener(this);
        add_comment_container.setOnClickListener(this);
        comment_camera.setOnClickListener(this);

        ter_height = termometer_container.getLayoutParams().height;

        //SET HEIGHT TERMOMETER
        Constants.HEIGHT_OF_TEMOMETER = (int) ((ter_height * 830) / 1050);


        autoCompleteAdapter = new AutoCompleteAdapter(this, R.layout.autocomplete_participant_item, participants_without_you);
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
                    destinationUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.charcoal_gray));
                    isUserInGroup = true;
                } else {
                    destinationUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.wrong_color));
                    isUserInGroup = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Variables.Group.setComments(new ArrayList<Comment>());
        commentsRecyclerView = (RecyclerView) findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setHasFixedSize(false);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ScrollY += dy;
                termometer_container.setTranslationY(ScrollY / -2);
                float alpha = (float) ScrollY / ter_height;
                if (alpha <= 1)
                    shadow.setAlpha((float) ScrollY / ter_height);
                Log.d("[SCROLL LISTENER]", dx + " " + dy + " " + ScrollY);
            }
        });

        loading_comments.setVisibility(View.VISIBLE);
        new CommentsController().getCommentsByGroup(this, Variables.Group.getId(), new ServiceCallbackInterface() {
            @Override
            public void onSuccess(String result) {
                loading_comments.setVisibility(View.GONE);
                adapter = new CommentsRecyclerAdapter(activity, Variables.Group.getComments());
                commentsRecyclerView.setAdapter(adapter);
                if (Variables.Group.getComments().size() > 0) {
                    commentsRecyclerView.setVisibility(View.VISIBLE);
                } else
                    no_comments.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String result) {
                adapter = new CommentsRecyclerAdapter(activity, Variables.Group.getComments());
                commentsRecyclerView.setAdapter(adapter);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        paintTermometer();
    }

    @Override
    public void onBackPressed() {
        if (!isAddCommentShowed)
            super.onBackPressed();
        else
            hideAddComment();

    }

    private void paintTermometer() {
        participants_without_you.clear();
        users_container.removeAllViews();
        for (Participant user : Variables.Group.getParticipants()) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View user_termometer_view = inflater.inflate(R.layout.termometer_user, users_container, false);

            user_termometer_view.setTag(user.getUserID());

            ImageView dashboard_user_image = (ImageView) user_termometer_view.findViewById(R.id.dashboard_user_image);
            TextView user_name = (TextView) user_termometer_view.findViewById(R.id.user_name);

            if (user.getUserID() == Variables.User.getUserID())
                user_name.setText(getString(R.string.you) + " (" + user.getMolopuntos() + " Mp)");
            else {
                user_name.setText(Tools.cropNameSurname(user.getName()) + " (" + user.getMolopuntos() + " Mp)");
                participants_without_you.add(user);
            }
            try {
                byte[] imageByteArray = Base64.decode(user.getImageBase64(), Base64.DEFAULT);

                Glide.with(this)
                        .load(imageByteArray)
                        .asBitmap()
                        .into(dashboard_user_image);

            } catch (Exception e) {
                dashboard_user_image.setImageResource(R.drawable.user_icon);
            }

            users_container.addView(user_termometer_view);
            users_container.getChildAt(users_container.getChildCount() - 1).setTranslationY(getPositionUser(user.getMolopuntos()));
        }

        autoCompleteAdapter = new AutoCompleteAdapter(this, R.layout.autocomplete_participant_item, participants_without_you);
        destinationUser.setAdapter(autoCompleteAdapter);
    }

    private Boolean userIsInGroup(String name) {
        for (Participant participant : participants_without_you)
            if (participant.getName().equals(name))
                return true;
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_comment:
                showAddComment();
                return true;
            case R.id.action_group_info:
                Intent group_detail = new Intent(this, GroupDetailActivity.class);
                startActivity(group_detail);
                return true;
            case android.R.id.title:
                Snackbar.make(findViewById(R.id.termometer_container), "Titulo pulsado", Snackbar.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showAddComment();
                break;
            case R.id.send_button:

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

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

                        final ProgressDialog progress = ProgressDialog.show(this, "",
                                "Enviando...", true);

                        new CommentsController().addCommentToGroup(this, comment, new ServiceCallbackInterface() {
                            @Override
                            public void onSuccess(String result) {
                                progress.dismiss();

                                comment.setUserName(Variables.User.getName());
                                comment.setUserImage(Variables.User.getImageBase64());
                                comment.setDestinationUserName(participantToSend.getName());
                                comment.setDate(new Date());

                                comment.setComments(new ArrayList<Integer>());
                                comment.setLikes(new ArrayList<Integer>());

                                Variables.Group.getComments().add(0, comment);
                                adapter.notifyItemInserted(1);

                                if (commentsRecyclerView.getVisibility() == View.GONE) {
                                    commentsRecyclerView.setVisibility(View.VISIBLE);
                                    no_comments.setVisibility(View.GONE);
                                }

                                LastEvent lastEvent = new LastEvent();
                                lastEvent.setUserID(Variables.User.getUserID());
                                lastEvent.setUserName(Variables.User.getName());
                                lastEvent.setDestinationUserID(participantToSend.getUserID());
                                lastEvent.setDestinationUserName(participantToSend.getName());
                                lastEvent.setLastUpdate(new Date());
                                Variables.Group.setLastEvent(lastEvent);

                                hideAddComment();

                                for (Participant participant : Variables.Group.getParticipants())
                                    if (participant.getUserID() == comment.getDestinationUserID())
                                        participant.setMolopuntos(participant.getMolopuntos() + 1);

                                repositionedTermometer();
                                recalculateTermometer();
                            }

                            @Override
                            public void onFailure(String result) {
                                progress.dismiss();
                            }
                        });

                    } else
                        Toast.makeText(this, "Pero no lo envies vacío animal!", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(this, "Vota a alguien que exista!", Toast.LENGTH_SHORT).show();

                break;

            case R.id.add_comment_container:
                hideAddComment();
                break;
            case R.id.comment_camera:
                showImageDialog();
                break;
        }
    }


    private void repositionedTermometer() {
        ScrollY = commentsRecyclerView.getScrollY();

        termometer_container.setTranslationY(ScrollY / -2);
        float alpha = (float) ScrollY / ter_height;
        if (alpha <= 1)
            shadow.setAlpha((float) ScrollY / ter_height);
    }

    private void recalculateTermometer() {
        getHighestScore();

        for (int i = 0; i < users_container.getChildCount(); i++) {
            View view = users_container.getChildAt(i);

            int UserID = (int) view.getTag();
            int molopuntosUser = 0;

            for (Participant participant : Variables.Group.getParticipants())
                if (participant.getUserID() == UserID) {
                    molopuntosUser = participant.getMolopuntos();

                    TextView user_name = (TextView) view.findViewById(R.id.user_name);

                    if (participant.getUserID() == Variables.User.getUserID())
                        user_name.setText(getString(R.string.you) + " (" + participant.getMolopuntos() + " Mp)");
                    else
                        user_name.setText(Tools.cropNameSurname(participant.getName()) + " (" + participant.getMolopuntos() + " Mp)");

                    view.animate()
                            .translationY(getPositionUser(molopuntosUser))
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(700)
                            .start();
                }
        }
    }

    public void addLikePoint(int destinationUserID) {

        for (Participant participant : Variables.Group.getParticipants())
            if (participant.getUserID() == destinationUserID)
                participant.setMolopuntos(participant.getMolopuntos() + 1);

        recalculateTermometer();
    }

    private void showAddComment() {
//        fab.setVisibility(View.GONE);
        add_comment_container.setVisibility(View.VISIBLE);
        isAddCommentShowed = true;
        comment_bitmap = null;
    }

    private void hideAddComment() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fab.getWindowToken(), 0);

        isAddCommentShowed = false;
//        fab.setVisibility(View.VISIBLE);
        add_comment_container.setVisibility(View.GONE);

        destinationUser.setText("");
        comment_text.setText("");
        comment_bitmap = null;
        comment_image.setImageBitmap(null);
    }

    private int getPositionUser(int molopuntos) {
        int relative_position = (molopuntos * Constants.HEIGHT_OF_TEMOMETER) / highestScore;
        return Constants.HEIGHT_OF_TEMOMETER - relative_position;
    }

    public int getHighestScore() {
        for (Participant user : Variables.Group.getParticipants())
            if (user.getMolopuntos() > highestScore) highestScore = user.getMolopuntos();

        return highestScore;
    }

    public void showCommentsDialog(int commentID, boolean needFocus, ArrayList<Integer> likes) {
        CommentsDialogFragment commentsDialogFragment = new CommentsDialogFragment();

        Bundle args = new Bundle();
        args.putInt("commentID", commentID);
        args.putBoolean("focus", needFocus);
        args.putString("likes", new Gson().toJson(likes));
        commentsDialogFragment.setArguments(args);

        commentsDialogFragment.show(getFragmentManager(), "COMMENTS");
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
                    Snackbar.make(add_comment_container, "Sin permiso no se puede usar la camara.", Snackbar.LENGTH_SHORT).show();

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
                    comment_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    comment_image.setImageBitmap(comment_bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_INPUT) {
                comment_bitmap = handleBigCameraPhoto();
                comment_image.setImageBitmap(comment_bitmap);
            }
        }
    }
}
