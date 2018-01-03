package com.bsalazar.molonometro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
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
import com.bsalazar.molonometro.area_adjust.AddFriendsActivity;
import com.bsalazar.molonometro.area_home.ContactsFragment;
import com.bsalazar.molonometro.area_home.GroupsFragment;
import com.bsalazar.molonometro.area_new_group.NewGroupActivity;
import com.bsalazar.molonometro.area_register.RegisterActivity;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Memo;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.UserIdJson;
import com.bsalazar.molonometro.rest.json.UserJson;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public FloatingActionButton fab;
    public float initialFabPosition;
    public Point size;

    private TextView notifText;
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

        //Refresh user if don't exist
        if(Variables.User == null){
            String rememberedUser = Memo.doYouRemember(this);
            if(rememberedUser != null){
                Gson gson = new Gson();
                Variables.User = gson.fromJson(rememberedUser, User.class);

//                if(Variables.User.getImageURL() != null)
//                    Variables.User.setImageURL(Tools.decodeBase64(Variables.User.getImageURL()));
            }
        }

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

        getContacts(true);

        // Save the screen size
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        size = new Point();
        display.getSize(size);

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("REM", false)){
            refreshNotificationBubble();
        }

        new UserController().updateFirebaseToken(this, getFirebaseToken(), null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static final int REQUEST_ACTUALIZE = 1;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_friend_request);
        MenuItemCompat.setActionView(item, R.layout.notification_bubble);

        View notif = item.getActionView();
        if(notif != null){
            notifText = (TextView) notif.findViewById(R.id.number_notification);
            notifText.setVisibility(View.GONE);

            notif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new UserController().getRequest(new UserJson(Variables.User.getUserID()), new ServiceCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            startActivityForResult(new Intent(getApplication(), AddFriendsActivity.class), REQUEST_ACTUALIZE);
                        }
                    });
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivityForResult(new Intent(this, AccountActivity.class), REQUEST_EXIT);
                return true;

            case R.id.action_refresh:
                if (main_view_pager.getCurrentItem() == 0)
                    refreshGroups(false);
                else if (main_view_pager.getCurrentItem() == 1)
                    getContacts(false);
                return true;

//            case R.id.action_dummy:
//                Constants.restController.getService().dummyService(new PushTestJson()
//                        , new Callback<String>() {
//                            @Override
//                            public void success(String userJson, Response response) {
//                                Log.d("[PRUEBA MD5]", userJson);
//                            }
//
//                            @Override
//                            public void failure(RetrofitError error) {
//
//                            }
//                        });
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int REQUEST_EXIT = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(this, RegisterActivity.class));
                this.finish();
            }
        } else if (requestCode == REQUEST_ACTUALIZE){
            if (resultCode == RESULT_OK){
                getContacts(false);
                refreshNotificationBubble();
            }
        }
    }

    private void refreshNotificationBubble(){
        new UserController().getUser(new ServiceCallback() {
            @Override
            public void onSuccess(Object result) {
                if(Variables.User.getNumRequest() > 0){
                    notifText.setVisibility(View.VISIBLE);
                    notifText.setText(String.valueOf(Variables.User.getNumRequest()));
                } else
                    notifText.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void refreshGroups(final boolean firstTime) {
        new GroupController().getGroupsByUser(this, new UserIdJson(Variables.User.getUserID()), new ServiceCallback() {
            @Override
            public void onSuccess(Object result) {
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
                Variables.createGroupJson = null;
                startActivity(new Intent(this, NewGroupActivity.class));
                break;
        }
    }

    private void getContacts(final boolean firstTime) {

        new UserController().getFriends(new UserJson(Variables.User.getUserID()), new ServiceCallback() {
            @Override
            public void onSuccess(Object result) {
                adapter.updateContacts();
                if(firstTime){
                    contactsReady = true;
                    showContent();
                    refreshGroups(true);
                }
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
