package com.bsalazar.molonometro.area_home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.MainActivity;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Tools;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class ContactsRecyclerAdapter  extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder> {

    private Context mContext;
    private ArrayList<Contact> contacts;

    public ContactsRecyclerAdapter(Context context, ArrayList<Contact> contacts) {
        this.mContext = context;
        this.contacts = contacts;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        final Contact contact = contacts.get(position);

        holder.contact_name.setText(contact.getName());

        if (contact.isInApp()){

            holder.item_detail.setText(contact.getState());
            try{
                byte[] imageByteArray = Base64.decode(contact.getImageBase64(), Base64.DEFAULT);

                Glide.with(mContext)
                        .load(imageByteArray)
                        .asBitmap()
                        .into(holder.contact_image);

            }catch (Exception e){
                holder.contact_image.setImageResource(R.drawable.user_icon);
            }

            holder.invite_contact_button.setVisibility(View.GONE);

        } else {
            holder.item_detail.setText(Tools.formatPhone(contact.getPhone()));
            holder.contact_image.setImageResource(R.drawable.user_icon);
            holder.invite_contact_button.setVisibility(View.VISIBLE);
        }

        holder.invite_contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).shareMolonometro();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView contact_image;
        TextView contact_name;
        TextView item_detail;
        TextView invite_contact_button;

        ContactViewHolder(View itemView) {
            super(itemView);

            contact_image = (ImageView) itemView.findViewById(R.id.contact_image);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            item_detail = (TextView) itemView.findViewById(R.id.item_detail);
            invite_contact_button = (TextView) itemView.findViewById(R.id.invite_contact_button);
        }
    }
}
