package com.bsalazar.molonometro.area_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Contact;

import java.util.ArrayList;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts = new ArrayList<>();
    private int resourceId;
    private Context mContext;

    public ContactsAdapter(Context context, int resource, ArrayList<Contact> contacts) {
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

        Contact contact = contacts.get(position);

        TextView group_name = (TextView) rootView.findViewById(R.id.group_name);
        TextView item_detail = (TextView) rootView.findViewById(R.id.item_detail);
        ImageView group_image = (ImageView) rootView.findViewById(R.id.group_image);

        group_name.setText(contact.getPhoneDisplayName());
        item_detail.setText(contact.getPhoneNumber());
        group_image.setImageBitmap(contact.getUserIcon());

        return rootView;
    }

}
