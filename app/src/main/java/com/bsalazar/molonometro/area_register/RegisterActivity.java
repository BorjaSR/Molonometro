package com.bsalazar.molonometro.area_register;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.firebase.SetFirebaseTokenThread;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Memo;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.UserJson;
import com.bsalazar.molonometro.rest.services.RestController;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText phone_for_register, user_name_for_register;
    private TextView register_here;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Constants.restController = new RestController();

        register_here = (TextView) findViewById(R.id.register_here);
        phone_for_register = (EditText) findViewById(R.id.phone_for_register);
        user_name_for_register = (EditText) findViewById(R.id.user_name_for_register);
        TextView continue_button = (TextView) findViewById(R.id.continue_button);
        FloatingActionButton next = (FloatingActionButton) findViewById(R.id.next);
        next.setVisibility(View.GONE);

        next.setOnClickListener(this);
        continue_button.setOnClickListener(this);
        register_here.setOnClickListener(this);

        // Load User ID:1
//        User user = new User();
//        user.setUserID(2);
//        user.setPhone("666333999");
//        user.setName("Nexus Grande");
//        user.setState("avatar de comer");
//
//        Gson gson2 = new Gson();
//        String userStringJson = gson2.toJson(user);
//        Memo.rememberMe(this, userStringJson);

        String rememberedUser = Memo.doYouRemember(this);
        if (rememberedUser != null && !rememberedUser.equals(Memo.NOT_SAVE)) {
            Gson gson = new Gson();
            Variables.User = gson.fromJson(rememberedUser, User.class);

            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.putExtra("REM", true);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        CreateUserJson createUserJson = new CreateUserJson();

        switch (id) {
            case R.id.next:
//                createUserJson.setName(user_name_for_register.getText().toString());
//                createUserJson.setPhone(phone_for_register.getText().toString());
//
//                new UserController().createUser(this, createUserJson);
                break;

            case R.id.register_here:
                createUserJson.setPassword(user_name_for_register.getText().toString());
                createUserJson.setEmail(phone_for_register.getText().toString());

                new UserController().createUser(createUserJson, new ServiceCallback() {
                    @Override
                    public void onSuccess(String result) {

                        Memo.rememberMe(getApplicationContext(), new Gson().toJson(Variables.User));

                        new SetFirebaseTokenThread(getApplicationContext()).start();

                        startActivity(new Intent(getApplicationContext(), SetFirstProfileDataActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(String result) {
                        Toast.makeText(getApplicationContext(), "KO creando al usuario", Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            case R.id.continue_button:
                UserJson userJson = new UserJson();
                userJson.setEmail(phone_for_register.getText().toString());
                userJson.setPassword(user_name_for_register.getText().toString());


                new UserController().login(userJson, new ServiceCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Variables.User.setUserID(Integer.valueOf(result));
                        new UserController().getUser(new ServiceCallback() {
                            @Override
                            public void onSuccess(String result) {

                                Memo.rememberMe(getApplicationContext(), new Gson().toJson(Variables.User));


                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.putExtra("REM", true);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String result) {
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
