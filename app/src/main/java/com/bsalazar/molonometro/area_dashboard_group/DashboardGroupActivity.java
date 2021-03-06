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

public class DashboardGroupActivity extends AppCompatActivity {

    private float ter_height;
    private int highestScore = 1;
    private RelativeLayout termometer_container;
    private RelativeLayout users_container;
    private LinearLayout shadow;
    private RecyclerView commentsRecyclerView;
    private CommentsRecyclerAdapter adapter;

    private FloatingActionButton fab;
    private TextView no_comments;
    private ProgressBar loading_comments;

    private int ScrollY = 0;

    private Activity activity;

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

        no_comments = (TextView) findViewById(R.id.no_comments);
        loading_comments = (ProgressBar) findViewById(R.id.loading_comments);

        ter_height = termometer_container.getLayoutParams().height;

        //SET HEIGHT TERMOMETER
        Constants.HEIGHT_OF_TEMOMETER = (int) ((ter_height * 830) / 1050);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard_group, menu);
        return true;
    }
    AddCommentDialogFragment addCommentDialogFragment;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_comment:
//                showAddComment();
                addCommentDialogFragment = new AddCommentDialogFragment();
                addCommentDialogFragment.show(getFragmentManager(), "ADD_COMMENT");
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

    private void paintTermometer() {
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
    }


    public void addComment(Comment comment){
        Variables.Group.getComments().add(0, comment);
        adapter.notifyItemInserted(1);

        if (commentsRecyclerView.getVisibility() == View.GONE) {
            commentsRecyclerView.setVisibility(View.VISIBLE);
            no_comments.setVisibility(View.GONE);
        }

        LastEvent lastEvent = new LastEvent();
        lastEvent.setUserID(Variables.User.getUserID());
        lastEvent.setUserName(Variables.User.getName());
        lastEvent.setDestinationUserID(comment.getDestinationUserID());
        lastEvent.setDestinationUserName(comment.getDestinationUserName());
        lastEvent.setLastUpdate(new Date());
        Variables.Group.setLastEvent(lastEvent);

        for (Participant participant : Variables.Group.getParticipants())
            if (participant.getUserID() == comment.getDestinationUserID())
                participant.setMolopuntos(participant.getMolopuntos() + 1);

        repositionedTermometer();
        recalculateTermometer();
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
}
