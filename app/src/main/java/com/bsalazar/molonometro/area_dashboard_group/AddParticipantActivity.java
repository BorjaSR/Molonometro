package com.bsalazar.molonometro.area_dashboard_group;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.adapters.AddParticipantAdapter;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.json.AddUserToGroupJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;

import java.util.ArrayList;

/**
 * Created by bsalazar on 30/05/2017.
 */

public class AddParticipantActivity extends AppCompatActivity implements View.OnClickListener  {

    private ArrayList<Contact> filteredContacts = new ArrayList<>();

    private RecyclerView contacts_recycler;
    private AddParticipantAdapter adapterRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_participant_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        for (Contact contact : Variables.contactsWithApp)
            filteredContacts.add(contact);

        contacts_recycler = (RecyclerView) findViewById(R.id.contacts_recycler);
        contacts_recycler.setHasFixedSize(true);
        contacts_recycler.setLayoutManager(new LinearLayoutManager(this));
        adapterRecycler = new AddParticipantAdapter(this, filteredContacts);
        contacts_recycler.setAdapter(adapterRecycler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_group, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Variables.search_for_add_participant_to_group = newText;
                filterResults(newText);
                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }

    private void filterResults(String query) {
        filteredContacts.clear();
        for (Contact contact : Variables.contactsWithApp)
            if (contact.getName().contains(query))
                filteredContacts.add(contact);

        adapterRecycler.notifyDataSetChanged();
    }

    public void showAddParticipantDialog(final Contact contact){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(contact.getName())
                .setMessage(String.format(getString(R.string.sure_to_add_it), Variables.Group.getName()))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AddUserToGroupJson addUserToGroupJson = new AddUserToGroupJson();
                        addUserToGroupJson.setUserID(Variables.User.getUserID());
                        addUserToGroupJson.setGroupID(Variables.Group.getId());
                        addUserToGroupJson.setContactID(contact.getUserID());

                        new GroupController().addUserToGroup(getApplicationContext(), addUserToGroupJson, new ServiceCallbackInterface() {
                            @Override
                            public void onSuccess(String result) {
                                Participant participant = new Participant();
                                participant.setUserID(contact.getUserID());
                                participant.setName(contact.getName());
                                participant.setImageBase64(contact.getImageBase64());
                                participant.setPhone(contact.getPhone());
                                participant.setMolopuntos(0);
                                participant.setState(contact.getState());
                                participant.setAdmin(false);

                                Variables.Group.getParticipants().add(participant);
                                finish();
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.drawable.user_icon_green)
                .show();
    }
}
