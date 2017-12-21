package com.bsalazar.molonometro.area_dashboard_group;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.adapters.CommonGroupsRecyclerAdapter;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.ContactController;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.GetContactDetailJson;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by bsalazar on 08/09/2017.
 */

public class ContactDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsing;
    private LinearLayout loading_progress, content;
    private ImageView contact_image;
    private TextView contact_molopuntos, contact_state, contact_name, no_common_groups;
    private RecyclerView common_groups_recycler;

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }

        int contactID = getIntent().getExtras().getInt("contactID", -1);
        for (Contact contactAux : Variables.contacts)
            if (contactAux.getUserID() == contactID) {
                contact = contactAux;
                break;
            }

        content = (LinearLayout) findViewById(R.id.content);
        loading_progress = (LinearLayout) findViewById(R.id.loading_progress);
        contact_molopuntos = (TextView) findViewById(R.id.contact_molopuntos);
        contact_state = (TextView) findViewById(R.id.contact_state);
        contact_name = (TextView) findViewById(R.id.contact_name);
        no_common_groups = (TextView) findViewById(R.id.no_common_groups);
        common_groups_recycler = (RecyclerView) findViewById(R.id.common_groups_recycler);

        setToolbar();
        setCollapsing();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final GetContactDetailJson contactJson = new GetContactDetailJson();
        contactJson.setContactID(contact.getUserID());
        contactJson.setUserID(Variables.User.getUserID());
        new ContactController().getContactByID(getApplicationContext(), contactJson, new ServiceCallback() {
            @Override
            public void onSuccess(String result) {
                ContactJson contactResponseJson = new Gson().fromJson(result, ContactJson.class);
                contact.setMolopuntos(contactResponseJson.getMolopuntos());
                contact.setEmail(contactResponseJson.getEmail());
                contact.setUserName(contactResponseJson.getUserName());

                if (contactResponseJson.getCommonGroups() == null)
                    contact.setCommonGroups(new ArrayList<Integer>());
                else
                    contact.setCommonGroups(contactResponseJson.getCommonGroups());

                collapsing.setTitle(contact.getUserName());
                setView();
            }
        });
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

        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        contact_image = (ImageView) findViewById(R.id.contact_image);

        final String imageBase64 = contact.getImageBase64();
        if (imageBase64 != null) {
            byte[] imageByteArray = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            contact_image.setImageBitmap(bmp);

            Palette p = Palette.from(bmp).generate();
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(Tools.brightnessDown(p.getDominantSwatch().getRgb()));
                collapsing.setContentScrimColor(p.getDominantSwatch().getRgb());
            }

        } else {
            contact_image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.user_icon));
        }

        contact_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showImageDialog();
            }
        });

        collapsing.setTitle(contact.getUserName());
        collapsing.setExpandedTitleTextAppearance(R.style.CollapsingTextDetailStyle);
        collapsing.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsing.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return true;
    }

    private void setView() {
        contact_molopuntos.setText(contact.getMolopuntos() + " Molopuntos");
        contact_state.setText(contact.getState());
        contact_name.setText(Tools.formatPhone(contact.getName()));

        if (contact.getCommonGroups().size() > 0) {
            ArrayList<Group> groups = new ArrayList<>();
            for (Integer groupID : contact.getCommonGroups())
                for (Group group : Variables.groups)
                    if (group.getId() == groupID) {
                        groups.add(group);
                        break;
                    }


            CommonGroupsRecyclerAdapter adapter = new CommonGroupsRecyclerAdapter(this, groups);
            common_groups_recycler.setHasFixedSize(false);
            common_groups_recycler.setLayoutManager(new LinearLayoutManager(this));
            common_groups_recycler.setAdapter(adapter);

        } else {
            common_groups_recycler.setVisibility(View.GONE);
            no_common_groups.setVisibility(View.VISIBLE);
        }

        content.setVisibility(View.VISIBLE);
        loading_progress.setVisibility(View.GONE);
    }
}
