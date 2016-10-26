package com.bsalazar.molonometro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.bsalazar.molonometro.area_home.MainScreenFragment;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int actualFragment = Constants.FRAG_ID_MAIN_SCREEN;
    private HashMap<Integer, Fragment> fragments = new HashMap<>();
    private FragmentManager fragmentManager;
    public FloatingActionButton fab;
    public float initialFabPosition;
    private int fragmentContainterID;
    public Point size;

    private final int PERMISSION_RESULT_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        fragmentContainterID = R.id.fragment_container;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        initialFabPosition = fab.getTranslationX();
        fab.setOnClickListener(this);

        fragments.put(Constants.FRAG_ID_MAIN_SCREEN, new MainScreenFragment());
        fragments.put(Constants.FRAG_ID_DASHBOARD_GROUP, new DashboardGroupFragment());

        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(fragmentContainterID, fragments.get(actualFragment))
                .commit();

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            getContacts();
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
    }

    @Override
    public void onBackPressed() {
        switch (actualFragment) {
            case Constants.FRAG_ID_DASHBOARD_GROUP:
                actualFragment = Constants.FRAG_ID_MAIN_SCREEN;
                super.onBackPressed();
                break;
            case Constants.FRAG_ID_MAIN_SCREEN:
                finish();
                break;
        }
    }

    public void changeFragment(int destination_fragment) {
        if (actualFragment != destination_fragment) {

            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.fragment_container, fragments.get(destination_fragment))
                    .addToBackStack(null)
                    .commit();
            actualFragment = destination_fragment;
        }
    }

    private void getContacts() {

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

        Bitmap userIcon = Tools.getRoundedCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user_icon));
        Variables.contacts.clear();
        while (contactsCursor.moveToNext()) {
            boolean isInList = false;
            for (Contact contact : Variables.contacts)
                if (contact.getPhoneDisplayName().equals(contactsCursor.getString(1)))
                    isInList = true;

            if (!isInList)
                Variables.contacts.add(new Contact(userIcon, contactsCursor.getString(1), contactsCursor.getString(2)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
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
                    getContacts();

                } else {
                    this.finish();
                }
                break;
            default:
                break;
        }
    }
}
