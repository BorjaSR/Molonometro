package com.bsalazar.molonometro.area_new_group;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;

/**
 * Created by bsalazar on 24/02/2017.
 */

public class FinishGroupActivity extends AppCompatActivity {

    private TextView contacts_in_group;
    private EditText group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_group_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        contacts_in_group = (TextView) findViewById(R.id.contacts_in_group);
        group_name = (EditText) findViewById(R.id.group_name);

        String contacts_in_group_txt = "";
        for(Integer id : Variables.createGroupJson.getContacts())
            contacts_in_group_txt += id + ", ";

        contacts_in_group.setText(contacts_in_group_txt);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finish_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_finish:
                if(group_name.getText().toString().length() != 0){
                    Variables.createGroupJson.setGroupName(group_name.getText().toString());
                    Variables.createGroupJson.setGroupImage("");

                    new GroupController().createGroup(this, Variables.createGroupJson, new ServiceCallbackInterface() {
                        @Override
                        public void onSuccess(String result) {
                            finish();
                        }

                        @Override
                        public void onFailure(String result) {

                        }
                    });
                }
                break;
        }
        return true;
    }
}
