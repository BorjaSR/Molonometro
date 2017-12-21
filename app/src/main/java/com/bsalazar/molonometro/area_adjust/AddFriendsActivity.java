package com.bsalazar.molonometro.area_adjust;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_adjust.adapters.ContactsFindedAdapter;
import com.bsalazar.molonometro.area_adjust.adapters.PendingRequestAdapter;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by bsalazar on 18/12/17.
 */

public class AddFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText search_edit;
    private LinearLayout loading, layout_pending_request;
    private RecyclerView contacts_finded_recycler, pending_request_recycler;
    private ContactsFindedAdapter adapter;
    private PendingRequestAdapter pendingRequestAdapter;
    private ArrayList<Contact> contactsFinded = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends_activity);

        search_edit = (EditText) findViewById(R.id.search_edit);
        loading = (LinearLayout) findViewById(R.id.loading);
        layout_pending_request = (LinearLayout) findViewById(R.id.layout_pending_request);
        contacts_finded_recycler = (RecyclerView) findViewById(R.id.contacts_finded_recycler);
        pending_request_recycler = (RecyclerView) findViewById(R.id.pending_request_recycler);


        pendingRequestAdapter = new PendingRequestAdapter(getApplicationContext(), Variables.User.getFriendRquests());
        pending_request_recycler.setLayoutManager(new LinearLayoutManager(this));
        pending_request_recycler.setAdapter(pendingRequestAdapter);

        adapter = new ContactsFindedAdapter(getApplicationContext(), contactsFinded);
        contacts_finded_recycler.setLayoutManager(new LinearLayoutManager(this));
        contacts_finded_recycler.setAdapter(adapter);

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_edit.getText().toString();

                if (text.length() == 0) {
                    contactsFinded.clear();
                    adapter = new ContactsFindedAdapter(getApplicationContext(), contactsFinded);
                    contacts_finded_recycler.setAdapter(adapter);

                    showPendingRequest();

                } else if (text.length() % 3 == 0) {
                    requestSearch(text);
                }
            }
        });

        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requestSearch(search_edit.getText().toString());

                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return true;
                }

                return false;
            }
        });
    }

    private void requestSearch(String text){
        showLoading();
        new UserController().searchUsers(text, new ServiceCallback() {
            @Override
            public void onSuccess(String result) {
                showSearchResults();
                Type contactTypeList = new TypeToken<ArrayList<Contact>>() {}.getType();
                contactsFinded = new Gson().fromJson(result, contactTypeList);
                adapter = new ContactsFindedAdapter(getApplicationContext(), contactsFinded);
                contacts_finded_recycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(String result) {
                showSearchResults();
                contactsFinded.clear();
                adapter = new ContactsFindedAdapter(getApplicationContext(), contactsFinded);
                contacts_finded_recycler.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    private void showLoading(){
        loading.setVisibility(View.VISIBLE);
        contacts_finded_recycler.setVisibility(View.GONE);
        layout_pending_request.setVisibility(View.GONE);
    }

    private void showSearchResults(){
        loading.setVisibility(View.GONE);
        contacts_finded_recycler.setVisibility(View.VISIBLE);
        layout_pending_request.setVisibility(View.GONE);
    }

    private void showPendingRequest(){
        loading.setVisibility(View.GONE);
        contacts_finded_recycler.setVisibility(View.GONE);
        layout_pending_request.setVisibility(View.VISIBLE);
    }
}
