package com.bsalazar.molonometro.area_new_group;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.TransitionManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.CreateGroupJson;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar ab;

    private ArrayList<Contact> filteredContacts = new ArrayList<>();
    private LinearLayout container_contacts_selected;
    private LinearLayout container_contacts_selected_2;
    private LinearLayout all;
    private LinearLayout container_next;
    private TextView next;

    private ContactsForGroupRecyclerAdapter adapterRecycler;
    private RecyclerView contacts_recycler;

    private ArrayList<Contact> contacts_selected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ab = getSupportActionBar();
        if (ab != null)
            ab.setSubtitle("Añadir participantes");

        container_contacts_selected = (LinearLayout) findViewById(R.id.container_contacts_selected);
        container_contacts_selected_2 = (LinearLayout) findViewById(R.id.container_contacts_selected_2);
        all = (LinearLayout) findViewById(R.id.all);
        container_next = (LinearLayout) findViewById(R.id.container_next);
        next = (TextView) findViewById(R.id.next);

        for (Contact contact : Variables.contactsWithApp)
            filteredContacts.add(contact);

        contacts_recycler = (RecyclerView) findViewById(R.id.groups_recycler);
        contacts_recycler.setHasFixedSize(true);
        contacts_recycler.setLayoutManager(new LinearLayoutManager(this));
        adapterRecycler = new ContactsForGroupRecyclerAdapter(this, filteredContacts);
        contacts_recycler.setAdapter(adapterRecycler);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Variables.createGroupJson = new CreateGroupJson();
                Variables.createGroupJson.setUserID(Variables.User.getUserID());

                ArrayList<Integer> contacts_selected_id = new ArrayList<>();

                for (Contact contact : contacts_selected)
                    contacts_selected_id.add(contact.getUserID());
                contacts_selected_id.add(Variables.User.getUserID());

                Variables.createGroupJson.setContacts(contacts_selected_id);

                startActivity(new Intent(getApplicationContext(), FinishGroupActivity.class));
                finish();
            }
        });

    }

    private HashMap<Integer, View> views_selected_contacts = new HashMap<>();
    public void addUserToSelection(final int indexUser) {
        if (isNotSelected(indexUser)) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            final View user_for_group = inflater.inflate(R.layout.contact_selected_for_group, container_contacts_selected, false);

            ImageView contact_for_group_user_image = (ImageView) user_for_group.findViewById(R.id.contact_for_group_user_image);
            TextView contact_for_group_user_name = (TextView) user_for_group.findViewById(R.id.contact_for_group_user_name);
            contact_for_group_user_name.setText(filteredContacts.get(indexUser).getName());

            try{
                Glide.with(this)
                        .load(Base64.decode(filteredContacts.get(indexUser).getImageBase64(), Base64.DEFAULT))
                        .asBitmap()
                        .into(contact_for_group_user_image);

            }catch (Exception e){
                contact_for_group_user_image.setImageResource(R.drawable.user_icon);
            }

            final int contactsID = filteredContacts.get(indexUser).getUserID();

            LinearLayout selected_user_layout = (LinearLayout) user_for_group.findViewById(R.id.selected_user_layout);
            selected_user_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TransitionManager.beginDelayedTransition(all);

                    container_contacts_selected.removeView(user_for_group);
                    views_selected_contacts.remove(contactsID);

                    if(container_contacts_selected.getChildCount() == 0)
                        container_contacts_selected.setVisibility(View.GONE);

                    for (int i = 0; i < contacts_selected.size(); i++)
                        if (contacts_selected.get(i).getUserID() == contactsID){
                            TransitionManager.beginDelayedTransition(all);
                            contacts_selected.remove(contacts_selected.get(i));
                            adapterRecycler.setContacts_selected(contacts_selected);
                            i--;
                        }

                    actualizeButtonAndTitle();
                }
            });

            TransitionManager.beginDelayedTransition(all);
            if(container_contacts_selected.getChildCount() == 0)
                container_contacts_selected.setVisibility(View.VISIBLE);

            container_contacts_selected.addView(user_for_group);
            views_selected_contacts.put(contactsID, user_for_group);

            contacts_selected.add(filteredContacts.get(indexUser));
            adapterRecycler.setContacts_selected(contacts_selected);

            actualizeButtonAndTitle();
        }
    }

    public void removeUserToSelection(final int indexUser){
        final int contactsID = filteredContacts.get(indexUser).getUserID();

        TransitionManager.beginDelayedTransition(all);
        container_contacts_selected.removeView(views_selected_contacts.get(contactsID));
        views_selected_contacts.remove(contactsID);

        if(container_contacts_selected.getChildCount() == 0)
            container_contacts_selected.setVisibility(View.GONE);


        for (int i = 0; i < contacts_selected.size(); i++)
            if (contacts_selected.get(i).getUserID() == contactsID){
                contacts_selected.remove(contacts_selected.get(i));
                adapterRecycler.setContacts_selected(contacts_selected);
                i--;
            }

        actualizeButtonAndTitle();
    }

    private void actualizeButtonAndTitle() {
        if (contacts_selected.size() > 0) {
            container_next.setVisibility(View.VISIBLE);

            if (contacts_selected.size() == 1)
                ab.setSubtitle(contacts_selected.size() + " participante");
            else
                ab.setSubtitle(contacts_selected.size() + " participantes");
        } else {
            container_next.setVisibility(View.GONE);
            ab.setSubtitle("Añadir participantes");
        }

    }

    private boolean isNotSelected(int indexUser) {
        final int contactID = filteredContacts.get(indexUser).getUserID();

        for (Contact contact : contacts_selected)
            if (contact.getUserID() == contactID)
                return false;
        return true;
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
                Variables.search_for_contacts_for_group = newText;
                filterResults(newText);
                return false;
            }
        });

        return true;
    }

    private void filterResults(String query) {
        filteredContacts.clear();
        for (Contact contact : Variables.contactsWithApp)
            if (contact.getName().contains(query))
                filteredContacts.add(contact);

        adapterRecycler.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:

                break;
        }
    }
}
