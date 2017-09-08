package com.bsalazar.molonometro.area_dashboard_group;

import android.app.Activity;
import android.content.Intent;
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
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.ContactController;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.GetContactDetailJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.google.gson.Gson;

/**
 * Created by bsalazar on 08/09/2017.
 */

public class ContactDetailActivity extends AppCompatActivity {


    private ImageView contact_image;
    private TextView contact_molopuntos, contact_state, contact_phone;

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
        for (Contact contactAux : Variables.contactsWithApp)
            if(contactAux.getUserID() == contactID) {
                contact = contactAux;
                break;
            }

        contact_molopuntos = (TextView) findViewById(R.id.contact_molopuntos);
        contact_state = (TextView) findViewById(R.id.contact_state);
        contact_phone = (TextView) findViewById(R.id.contact_phone);

        setToolbar();
        setCollapsing();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetContactDetailJson contactJson = new GetContactDetailJson();
        contactJson.setContactID(contact.getUserID());
        contactJson.setUserID(Variables.User.getUserID());
        new ContactController().getContactByID(getApplicationContext(), contactJson, new ServiceCallbackInterface() {
            @Override
            public void onSuccess(String result) {
                ContactJson contactResponseJson = new Gson().fromJson(result, ContactJson.class);
                contact.setMolopuntos(contactResponseJson.getMolopuntos());
                contact.setCommonGroups(contactResponseJson.getCommonGroups());

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

        final CollapsingToolbarLayout collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
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

        collapsing.setTitle(contact.getName());
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
        contact_phone.setText(Tools.formatPhone(contact.getPhone()));
    }
}
