package com.bsalazar.molonometro.area_new_group;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.general.Variables;

import java.util.ArrayList;

public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private ContactsForGroupAdapter adapter;
    private ArrayList<Contact> filteredContacts = new ArrayList<>();
    private LinearLayout container_contacts_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        for (Contact contact : Variables.contactsWithApp)
            filteredContacts.add(contact);

        ListView contact_for_new_group = (ListView) findViewById(R.id.contact_for_new_group);
        adapter = new ContactsForGroupAdapter(this, R.layout.contact_for_group_item, filteredContacts);
        contact_for_new_group.setAdapter(adapter);

        container_contacts_selected = (LinearLayout) findViewById(R.id.container_contacts_selected);
    }

    public void addUserToSelection(int indexUser){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View user_for_group = inflater.inflate(R.layout.contact_selected_for_group, container_contacts_selected, false);

        TextView contact_for_group_user_name = (TextView) user_for_group.findViewById(R.id.contact_for_group_user_name);
        contact_for_group_user_name.setText(filteredContacts.get(indexUser).getName());

        LinearLayout selected_user_layout = (LinearLayout) user_for_group.findViewById(R.id.selected_user_layout);
        selected_user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container_contacts_selected.removeView(user_for_group);
            }
        });

        container_contacts_selected.addView(user_for_group);
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
            if(contact.getName().contains(query))
                filteredContacts.add(contact);

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }else if (id == R.id.action_search) {
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
