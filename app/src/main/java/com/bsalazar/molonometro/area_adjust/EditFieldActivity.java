package com.bsalazar.molonometro.area_adjust;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.services.ServiceCallback;

/**
 * Created by bsalazar on 21/02/2017.
 */

public class EditFieldActivity extends AppCompatActivity {


    public final static String MODE = "MODE";
    public final static int EDIT_USER_NAME = 0;
    public final static int EDIT_USER_STATE = 1;
    public final static int EDIT_GROUP_NAME = 2;
    public final static int EDIT_NAME = 3;

    private String initial_String;
    private EditText edit_text;
    private int SELECTED_MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_field_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        edit_text = (EditText) findViewById(R.id.edit_text);


        SELECTED_MODE = getIntent().getIntExtra(MODE, EDIT_USER_NAME);

        switch (SELECTED_MODE){
            case EDIT_USER_NAME:
                setTitle(getString(R.string.edit_name));
                edit_text.setText(Variables.User.getUserName());
                initial_String = Variables.User.getUserName();
                break;
            case EDIT_NAME:
                setTitle(getString(R.string.edit_user_name));
                edit_text.setText(Variables.User.getName());
                initial_String = Variables.User.getName();
                break;
            case EDIT_USER_STATE:
                setTitle(getString(R.string.edit_state));
                edit_text.setText(Variables.User.getState());
                initial_String = Variables.User.getState();
                break;
            case EDIT_GROUP_NAME:
                setTitle(getString(R.string.edit_group_name));
                edit_text.setText(Variables.Group.getName());
                initial_String = Variables.Group.getName();
                break;
        }

        if(initial_String == null) initial_String = "";

        edit_text.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_field, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_finish:

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);

                if(!initial_String.equals(edit_text.getText().toString())){
                    switch (SELECTED_MODE){

                        case EDIT_USER_NAME:
                            new UserController().updateUserName(this, edit_text.getText().toString(), new ServiceCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    finish();
                                }

                                @Override
                                public void onFailure(String result) {
                                    Snackbar.make(edit_text, "Error actualizando nombre", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                            break;

                        case EDIT_NAME:
                            new UserController().updateName(this, edit_text.getText().toString(), new ServiceCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    finish();
                                }

                                @Override
                                public void onFailure(String result) {
                                    Snackbar.make(edit_text, "Error actualizando nombre", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                            break;

                        case EDIT_USER_STATE:
                            new UserController().updateUserState(this, edit_text.getText().toString(), new ServiceCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    finish();
                                }

                                @Override
                                public void onFailure(String result) {
                                    Snackbar.make(edit_text, "Error actualizando estado", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                            break;

                        case EDIT_GROUP_NAME:
                            GroupJson groupJson = new GroupJson();
                            groupJson.setGroupID(Variables.Group.getId());
                            groupJson.setName(edit_text.getText().toString());
                            groupJson.setFirebaseTopic(Variables.Group.getFirebaseTopic());

                            new GroupController().updateGroup(this, groupJson, new ServiceCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    finish();
                                }

                                @Override
                                public void onFailure(String result) {
                                    Snackbar.make(edit_text, "Error actualizando grupo", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                            break;
                    }
                } else
                    finish();

                return true;
        }
        return true;
    }
}
