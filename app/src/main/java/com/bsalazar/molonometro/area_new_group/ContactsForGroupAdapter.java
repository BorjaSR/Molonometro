package com.bsalazar.molonometro.area_new_group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.util.ArrayList;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class ContactsForGroupAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts = new ArrayList<>();
    private int resourceId;
    private Context mContext;

    private ArrayList<Contact> contacts_selected = new ArrayList<>();

    public ContactsForGroupAdapter(Context context, int resource, ArrayList<Contact> contacts) {
        super(context, resource, contacts);
        this.contacts = contacts;
        this.resourceId = resource;
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View rootView, ViewGroup parent) {

        if (rootView == null) {
            LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater1.inflate(resourceId, null);
        }

        final Contact contact = contacts.get(position);

        TextView contact_name_first_part = (TextView) rootView.findViewById(R.id.group_name_first_part);
        TextView match_contact_name = (TextView) rootView.findViewById(R.id.match_group_name);
        TextView contact_name_second_part = (TextView) rootView.findViewById(R.id.group_name_second_part);
        TextView item_detail = (TextView) rootView.findViewById(R.id.item_detail);
        ImageView group_image = (ImageView) rootView.findViewById(R.id.contact_image);



        if (Variables.search_for_contacts_for_group.equals("")) {
            contact_name_first_part.setText(contact.getName());
            contact_name_first_part.setVisibility(View.VISIBLE);
            match_contact_name.setVisibility(View.GONE);
            contact_name_second_part.setVisibility(View.GONE);
        } else {
            String[] parts = contact.getName().split(Variables.search_for_contacts_for_group);
            if(parts.length == 0){
                match_contact_name.setText(Variables.search_for_contacts_for_group);
                contact_name_first_part.setVisibility(View.GONE);
                contact_name_second_part.setVisibility(View.GONE);
            } else {
                contact_name_first_part.setText(parts[0]);
                match_contact_name.setText(Variables.search_for_contacts_for_group);

                String second = "";
                for(int i = 1; i < parts.length; i++)
                    second += parts[i];
                contact_name_second_part.setText(second);

                match_contact_name.setVisibility(View.VISIBLE);
                contact_name_second_part.setVisibility(View.VISIBLE);
            }
        }


        item_detail.setText(contact.getState());
        group_image.setImageResource(R.drawable.user_icon);


        final LinearLayout contact_for_new_group_layout = (LinearLayout) rootView.findViewById(R.id.contact_for_new_group_layout);
        contact_for_new_group_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewGroupActivity) mContext).addUserToSelection(position);
            }
        });

        return rootView;
    }

}
