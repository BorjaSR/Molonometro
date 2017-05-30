package com.bsalazar.molonometro.area_dashboard_group;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_adjust.EditFieldActivity;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class GroupDetailActivity extends AppCompatActivity {

    private final int GALERY_INPUT = 1;
    LinearLayout participants_container;

    private int highestScore = 1;
    private ImageView group_image;
    private Activity activity;

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

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.ctl);
        group_image = (ImageView) findViewById(R.id.group_image);

        String imageBase64 = Variables.Group.getImageBase64();
        if (imageBase64 != null) {
            byte[] imageByteArray = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            group_image.setImageBitmap(bmp);

        } else {
            group_image.setImageDrawable(getResources().getDrawable(R.drawable.group_icon));
        }

        group_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalery();
            }
        });

        ctl.setTitle(Variables.Group.getName());
        ctl.setExpandedTitleTextAppearance(R.style.text_detail_style);
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

        for (Participant participant : Variables.Group.getParticipants()) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            final View participant_view = inflater.inflate(R.layout.participant_group_item, participants_container, false);

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

            if(participant.isAdmin())
                admin_signal.setVisibility(View.VISIBLE);
            else
                admin_signal.setVisibility(View.GONE);

            participants_container.addView(participant_view);
        }
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
                    group_image.setImageBitmap(null);

                    GroupJson groupJson = new GroupJson();
                    groupJson.setGroupID(Variables.Group.getId());
                    groupJson.setImage(Tools.encodeBitmapToBase64(new_image));

                    new GroupController().updateGroupImage(this, groupJson, new ServiceCallbackInterface() {
                        @Override
                        public void onSuccess(String result) {

//                            Variables.Group.setImageBase64(Tools.encodeBitmapToBase64(new_image));

                            byte[] bitmapdata = Base64.decode(Variables.Group.getImageBase64(), Base64.DEFAULT);

                            Glide.with(getApplicationContext())
                                    .load(bitmapdata)
                                    .asBitmap()
                                    .dontAnimate()
                                    .listener(new MyRequestListener(activity, group_image))
                                    .into(group_image);


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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
