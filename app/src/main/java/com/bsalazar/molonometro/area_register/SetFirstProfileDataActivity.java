package com.bsalazar.molonometro.area_register;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.ContactsForGroupAdapter;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Memo;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.services.RestController;
import com.squareup.okhttp.internal.spdy.Variant;

import java.util.ArrayList;

public class SetFirstProfileDataActivity extends AppCompatActivity implements View.OnClickListener {

    public Point size;
    private ContactsForGroupAdapter adapter;
    private ArrayList<Contact> filteredContacts = new ArrayList<>();
    private LinearLayout container_contacts_selected;
    private EditText phone_for_register, user_name_for_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_first_profile_data_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Constants.restController = new RestController();

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
                EditText user_state = (EditText) findViewById(R.id.user_state);
                UpdateUserJson updateUserJson = new UpdateUserJson();
                updateUserJson.setUserID(Variables.User.getUserID());
                updateUserJson.setName(Variables.User.getName());
                updateUserJson.setState(user_state.getText().toString());

                new UserController().updateUser(this, updateUserJson);
                break;
        }
    }
}
