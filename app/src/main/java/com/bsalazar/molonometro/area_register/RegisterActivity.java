package com.bsalazar.molonometro.area_register;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bsalazar.molonometro.area_home.MainActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Memo;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.services.RestController;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText phone_for_register, user_name_for_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Constants.restController = new RestController();

        phone_for_register = (EditText) findViewById(R.id.phone_for_register);
        user_name_for_register = (EditText) findViewById(R.id.user_name_for_register);

        FloatingActionButton next = (FloatingActionButton) findViewById(R.id.next);
        next.setOnClickListener(this);

        String rememberedUser = Memo.doYouRemember(this);
        if(rememberedUser != null){
            Gson gson = new Gson();
            Variables.User = gson.fromJson(rememberedUser, User.class);

            if(Variables.User.getImageBase64() != null)
                Variables.User.setImage(Tools.decodeBase64(Variables.User.getImageBase64()));

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
            case R.id.next:
                CreateUserJson createUserJson = new CreateUserJson();
                createUserJson.setName(user_name_for_register.getText().toString());
                createUserJson.setPhone(phone_for_register.getText().toString());

                new UserController().createUser(this, createUserJson);
                break;
        }
    }
}
