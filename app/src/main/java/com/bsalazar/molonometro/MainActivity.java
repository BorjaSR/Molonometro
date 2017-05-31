package com.bsalazar.molonometro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.area_adjust.AccountActivity;
import com.bsalazar.molonometro.area_home.ContactsFragment;
import com.bsalazar.molonometro.area_home.GroupsFragment;
import com.bsalazar.molonometro.area_new_group.NewGroupActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.PhoneContact;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.ContactController;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.ContactsListJson;
import com.bsalazar.molonometro.rest.json.PushTestJson;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.json.UserIdJson;
import com.bsalazar.molonometro.rest.json.UserJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public FloatingActionButton fab;
    public float initialFabPosition;
    public Point size;

    private ViewPager main_view_pager;
    private MainScreenAdapter adapter;

    private final int PERMISSION_RESULT_READ_CONTACTS = 1;

    private boolean contactsReady = false;
    private boolean groupsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Constants.fragmentManager = getFragmentManager();

        TextView groups_button = (TextView) findViewById(R.id.groups_button);
        TextView contacts_button = (TextView) findViewById(R.id.contacts_button);
        final LinearLayout indicator_current_page = (LinearLayout) findViewById(R.id.indicator_current_page);

        main_view_pager = (ViewPager) findViewById(R.id.main_view_pager);
        adapter = new MainScreenAdapter(getSupportFragmentManager());
        main_view_pager.setAdapter(adapter);

        groups_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_view_pager.getCurrentItem() != 0)
                    main_view_pager.setCurrentItem(0);
            }
        });

        contacts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_view_pager.getCurrentItem() != 1)
                    main_view_pager.setCurrentItem(1);
            }
        });

        main_view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                fab.setTranslationX(positionOffsetPixels / 3);
                indicator_current_page.setX(positionOffsetPixels / 2);
                if (position == 1 && positionOffsetPixels == 0) {
                    indicator_current_page.setX(size.x / 2);
                    fab.setX(size.x);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        initialFabPosition = fab.getTranslationX();
        fab.setOnClickListener(this);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            getContacts(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSION_RESULT_READ_CONTACTS);
        }



        // Save the screen size
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        size = new Point();
        display.getSize(size);

        new UserController().updateFirebaseToken(this, getFirebaseToken(), null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AccountActivity.class));
            return true;
        } else if (id == R.id.action_refresh) {
            if (main_view_pager.getCurrentItem() == 0)
                refreshGroups(false);
            else if (main_view_pager.getCurrentItem() == 1)
                getContacts(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshGroups(final boolean firstTime) {
        new GroupController().getGroupsByUser(this, new UserIdJson(Variables.User.getUserID()), new ServiceCallbackInterface() {
            @Override
            public void onSuccess(String result) {
                adapter.updateGroups();
                if(firstTime){
                    groupsReady = true;
                    showContent();
                }
            }

            @Override
            public void onFailure(String result) {

            }
        });
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:

//                PushTestJson pushTestJson = new PushTestJson();
//                pushTestJson.setToken(Variables.User.getFirebaseToken());
//                pushTestJson.setType(0);
//
//                Constants.restController.getService().sendPushTest(pushTestJson
//                        , new Callback<Boolean>() {
//                            @Override
//                            public void success(Boolean result, Response response) {
//                                    Snackbar.make(v, result.toString(), Snackbar.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void failure(RetrofitError error) {
//                                Snackbar.make(v, error.toString(), Snackbar.LENGTH_SHORT).show();
//
//                            }
//                        });


                Variables.createGroupJson = null;
                startActivity(new Intent(this, NewGroupActivity.class));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RESULT_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts(true);
                } else {
                    this.finish();
                }
                break;
            default:
                break;
        }
    }

    private void getContacts(final boolean firstTime) {

        String[] projection = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Data.MIMETYPE}; //, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE

        String selectionClause = ContactsContract.Data.MIMETYPE + " = '" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND " +
                ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";


        Cursor contactsCursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selectionClause,
                null,
                sortOrder);

        ArrayList<PhoneContact> phoneContacts = new ArrayList<>();
        phoneContacts.clear();
        while (contactsCursor.moveToNext()) {
            boolean isInList = false;
            for (PhoneContact phoneContact : phoneContacts)
                if (phoneContact.getPhoneDisplayName().equals(contactsCursor.getString(1)))
                    isInList = true;

            String phone = contactsCursor.getString(2);
            phone = phone.replace(" ", "");
            if (phone.length() > 9) phone = phone.substring(phone.length() - 9);

            if (!isInList && !phone.equals(Variables.User.getPhone()))
                phoneContacts.add(new PhoneContact(contactsCursor.getString(1), phone));
        }

        ContactsListJson contactsListJson = new ContactsListJson();
        contactsListJson.setPhoneContacts(phoneContacts);
        new ContactController().checkContacts(this, contactsListJson, new ServiceCallbackInterface() {
            @Override
            public void onSuccess(String result) {
                adapter.updateContacts();
                if(firstTime){
                    contactsReady = true;
                    showContent();
                    refreshGroups(true);
                }
            }

            @Override
            public void onFailure(String result) {

            }
        });
    }

    public void shareMolonometro() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Descubre cual de tus amigos mola más con el Molonometro!!");
        startActivity(Intent.createChooser(intent, "Share with"));
    }

    public class MainScreenAdapter extends FragmentStatePagerAdapter {

        private GroupsFragment groupsFragment = new GroupsFragment();
        private ContactsFragment constantsFragment = new ContactsFragment();

        MainScreenAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = groupsFragment;
                    break;
                case 1:
                    fragment = constantsFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        void updateContacts() {
            constantsFragment.updateContactList();
        }

        void updateGroups() {
            groupsFragment.updateGroupList();
        }
    }

    public String getFirebaseToken() {

        // Get token
        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        String msg = " Token del dispositivo: " + token;
        Log.d(TAG, msg);

        if (Variables.User != null) Variables.User.setFirebaseToken(token);

        return token;
    }

    private void showContent(){
        if(contactsReady && groupsReady){
            RelativeLayout view_pager_container = (RelativeLayout) findViewById(R.id.view_pager_container);
            LinearLayout progress_main_contain = (LinearLayout) findViewById(R.id.progress_main_contain);

            TransitionManager.beginDelayedTransition(view_pager_container);
            progress_main_contain.setVisibility(View.GONE);
        }
    }
}
