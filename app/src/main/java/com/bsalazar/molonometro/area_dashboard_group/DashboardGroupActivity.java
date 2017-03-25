package com.bsalazar.molonometro.area_dashboard_group;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.adapters.AutoCompleteAdapter;
import com.bsalazar.molonometro.area_dashboard_group.adapters.CommentsRecyclerAdapter;
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.CommentsController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class DashboardGroupActivity extends AppCompatActivity implements View.OnClickListener {

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
    private ProgressBar loading_comments;

    private boolean isAddCommentShowed = false;
    private boolean isUSerInGroup = false;
    private Participant participantToSend;

    private int ScrollY = 0;

    private Activity activity;

    ArrayList<Participant> participants_without_you;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_group_fragment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.activity = this;

        final ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setTitle(Variables.Group.getName());

        getHighestScore();


        termometer_container = (RelativeLayout) findViewById(R.id.termometer_container);
        users_container = (RelativeLayout) findViewById(R.id.users_container);
        shadow = (LinearLayout) findViewById(R.id.shadow);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        add_comment_container = (LinearLayout) findViewById(R.id.add_comment_container);
        destinationUser = (AutoCompleteTextView) findViewById(R.id.destinationUser);
        send_button = (TextView) findViewById(R.id.send_button);
        no_comments = (TextView) findViewById(R.id.no_comments);
        comment_text = (EditText) findViewById(R.id.comment_text);
        loading_comments = (ProgressBar) findViewById(R.id.loading_comments);

        participants_without_you = new ArrayList<>();

        fab.setOnClickListener(this);
        send_button.setOnClickListener(this);
        add_comment_container.setOnClickListener(this);

        ter_height = termometer_container.getLayoutParams().height;

        //SET HEIGHT TERMOMETER
        Constants.HEIGHT_OF_TEMOMETER = (int) ((ter_height * 830) / 1050);

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


        final AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(this, R.layout.autocomplete_participant_item, participants_without_you);

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
                    isUSerInGroup = true;
                } else {
                    destinationUser.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.wrong_color));
                    isUSerInGroup = true;
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
    public void onBackPressed() {
        if (!isAddCommentShowed)
            super.onBackPressed();
        else
            hideAddComment();

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

                if (isUSerInGroup) {
                    if (comment_text.getText().toString().length() != 0) {
                        final Comment comment = new Comment();
                        comment.setGroupID(Variables.Group.getId());
                        comment.setUserID(Variables.User.getUserID());
                        comment.setDestinationUserID(participantToSend.getUserID());
                        comment.setText(comment_text.getText().toString());

                        new CommentsController().addCommentToGroup(this, comment, new ServiceCallbackInterface() {
                            @Override
                            public void onSuccess(String result) {
                                comment.setUserName(Variables.User.getName());
                                comment.setUserImage(Variables.User.getImageBase64());
                                comment.setDestinationUserName(participantToSend.getName());

                                Variables.Group.getComments().add(0, comment);
                                adapter.notifyItemInserted(1);

                                if (commentsRecyclerView.getVisibility() == View.GONE) {
                                    commentsRecyclerView.setVisibility(View.VISIBLE);
                                    no_comments.setVisibility(View.GONE);
                                }


                                hideAddComment();

                                for (Participant participant : Variables.Group.getParticipants())
                                    if (participant.getUserID() == comment.getDestinationUserID())
                                        participant.setMolopuntos(participant.getMolopuntos() + 1);

                                repositionedTermometer();
                                recalculateTermometer();
                            }

                            @Override
                            public void onFailure(String result) {

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
        }
    }

    //TODO pendiente de revisar
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
                            .setDuration(1000)
                            .start();
                }
        }
    }

    private void showAddComment() {
        fab.setVisibility(View.GONE);
        add_comment_container.setVisibility(View.VISIBLE);
        isAddCommentShowed = true;
    }

    private void hideAddComment() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fab.getWindowToken(), 0);

        isAddCommentShowed = false;
        fab.setVisibility(View.VISIBLE);
        add_comment_container.setVisibility(View.GONE);

        destinationUser.setText("");
        comment_text.setText("");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void enterReveal() {
        // previously invisible view
        final View myView = findViewById(R.id.text);

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
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

    public void showReplyDialog(final Comment comment) {

        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.reply_dialog, null);

        TextView reply_to = (TextView) dialoglayout.findViewById(R.id.reply_to);
        final EditText reply_text = (EditText) dialoglayout.findViewById(R.id.reply_text);
        TextView send_reply = (TextView) dialoglayout.findViewById(R.id.send_reply);

        reply_to.setText(comment.getUserName());

        send_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reply_text.getText().toString().length() != 0) {
                    Comment reply = new Comment();
                    reply.setCommentID(comment.getCommentID());
                    reply.setUserID(Variables.User.getUserID());
                    reply.setText(reply_text.getText().toString());

                    new CommentsController().addReplyToComment(getApplicationContext(), reply, new ServiceCallbackInterface() {
                        @Override
                        public void onSuccess(String result) {
                            if(result.equals("true")){
                                builder.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(String result) {
                            Toast.makeText(activity, "Oops! Ha ocurrido algún error", Toast.LENGTH_SHORT).show();
                            builder.dismiss();
                        }
                    });

                } else
                    Toast.makeText(activity, "Pero no lo envies vacío animal!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(dialoglayout);
        builder.show();
    }
}
