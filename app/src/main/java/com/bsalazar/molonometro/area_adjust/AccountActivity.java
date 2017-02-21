package com.bsalazar.molonometro.area_adjust;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;

/**
 * Created by bsalazar on 21/02/2017.
 */

public class AccountActivity extends AppCompatActivity {

    private ImageView profile_image;
    private TextView profile_user_name;
    private TextView profile_state;
    private TextView profile_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjust_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_user_name = (TextView) findViewById(R.id.profile_user_name);
        profile_state = (TextView) findViewById(R.id.profile_state);
        profile_phone = (TextView) findViewById(R.id.profile_phone);

        fillFields();

    }

    private void fillFields() {
        profile_image.setImageBitmap(Tools.getRoundedCroppedBitmap(Variables.User.getImage()));
        profile_user_name.setText(Variables.User.getName());
        profile_state.setText(Variables.User.getState());
        profile_phone.setText(Tools.formatPhone(Variables.User.getPhone()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()  ){
            case android.R.id.home :
                finish();
                return true;
        }
        return true;
    }

}
