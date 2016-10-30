package com.bsalazar.molonometro.area_home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.util.ArrayList;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class ContactsAdapter extends ArrayAdapter<UserJson> {

    private ArrayList<UserJson> contacts = new ArrayList<>();
    private int resourceId;
    private Context mContext;

    public ContactsAdapter(Context context, int resource, ArrayList<UserJson> contacts) {
        super(context, resource, contacts);
        this.contacts = contacts;
        this.resourceId = resource;
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View rootView, ViewGroup parent) {

        if(rootView == null){
            LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater1.inflate(resourceId, null);
        }

        UserJson contact = contacts.get(position);

        TextView group_name = (TextView) rootView.findViewById(R.id.group_name);
        TextView item_detail = (TextView) rootView.findViewById(R.id.item_detail);
        TextView invite_contact_button = (TextView) rootView.findViewById(R.id.invite_contact_button);
        ImageView group_image = (ImageView) rootView.findViewById(R.id.group_image);

        group_name.setText(contact.getName());

        if (contact.isInApp()){
            item_detail.setText(contact.getState());
            group_image.setImageResource(R.drawable.user_icon);
            invite_contact_button.setVisibility(View.GONE);

        } else {
            item_detail.setText(contact.getPhone());
            group_image.setImageResource(R.drawable.user_icon);
            invite_contact_button.setVisibility(View.VISIBLE);
        }

        invite_contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).shareMolonometro();
            }
        });

        return rootView;
    }

}
