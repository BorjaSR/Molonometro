package com.bsalazar.molonometro.area_new_group;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.adapters.ContactsForGroupAdapter;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Variables;

import java.util.ArrayList;

public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener {

    public Point size;
    private ContactsForGroupAdapter adapter;
    private ArrayList<Contact> filteredContacts = new ArrayList<>();
    private LinearLayout container_contacts_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        // Save the screen size
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        size = new Point();
        display.getSize(size);

        for (Contact contact : Variables.contacts)
            filteredContacts.add(contact);

        ListView contact_for_new_group = (ListView) findViewById(R.id.contact_for_new_group);
        adapter = new ContactsForGroupAdapter(this, R.layout.contact_for_group_item, Variables.contactsWithApp);
        contact_for_new_group.setAdapter(adapter);

        container_contacts_selected = (LinearLayout) findViewById(R.id.container_contacts_selected);
    }

    public void addUserToSelection(int indexUser){

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View my_next_booking = inflater.inflate(R.layout.contact_selected_for_group, container_contacts_selected, false);

        TextView contact_for_group_user_name = (TextView) my_next_booking.findViewById(R.id.contact_for_group_user_name);
        contact_for_group_user_name.setText(filteredContacts.get(indexUser).getPhoneDisplayName());

        LinearLayout selected_user_layout = (LinearLayout) my_next_booking.findViewById(R.id.selected_user_layout);
        selected_user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container_contacts_selected.removeView(my_next_booking);
            }
        });

        container_contacts_selected.addView(my_next_booking);
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
        for (Contact contact : Variables.contacts)
            if(contact.getPhoneDisplayName().contains(query))
                filteredContacts.add(contact);

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            case R.id.fab:

                break;
        }
    }
}
