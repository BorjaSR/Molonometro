package com.bsalazar.molonometro.area_new_group;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.MainActivity;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class ContactsForGroupRecyclerAdapter extends RecyclerView.Adapter<ContactsForGroupRecyclerAdapter.ContactsForGroupViewHolder> {

    private Context mContext;
    private ArrayList<Contact> contacts;

    private ArrayList<Integer> contacts_selected = new ArrayList<>();

    public ContactsForGroupRecyclerAdapter(Context context, ArrayList<Contact> contacts) {
        this.mContext = context;
        this.contacts = contacts;
    }

    @Override
    public ContactsForGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_for_group_item, parent, false);
        return new ContactsForGroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ContactsForGroupViewHolder holder, int position) {

        final Contact contact = contacts.get(position);

        if (Variables.search_for_contacts_for_group.equals("")) {
            holder.contact_name_first_part.setText(contact.getName());
            holder.contact_name_first_part.setVisibility(View.VISIBLE);
            holder.match_contact_name.setVisibility(View.GONE);
            holder.contact_name_second_part.setVisibility(View.GONE);

        } else {
            String[] parts = contact.getName().split(Variables.search_for_contacts_for_group);
            if (parts.length == 0) {
                holder.match_contact_name.setText(Variables.search_for_contacts_for_group);
                holder.contact_name_first_part.setVisibility(View.GONE);
                holder.contact_name_second_part.setVisibility(View.GONE);
            } else {
                holder.contact_name_first_part.setText(parts[0]);
                holder.match_contact_name.setText(Variables.search_for_contacts_for_group);

                String second = "";
                for (int i = 1; i < parts.length; i++)
                    second += parts[i];
                holder.contact_name_second_part.setText(second);

                holder.match_contact_name.setVisibility(View.VISIBLE);
                holder.contact_name_second_part.setVisibility(View.VISIBLE);
            }
        }

        holder.item_detail.setText(contact.getState());

        try {
            Glide.with(mContext)
                    .load(Base64.decode(contact.getImageBase64(), Base64.DEFAULT))
                    .asBitmap()
                    .listener(new MyRequestListener(mContext, holder.contact_image))
                    .into(holder.contact_image);

        } catch (Exception e) {
            holder.contact_image.setImageResource(R.drawable.user_icon);
        }

        if (contacts_selected.contains(contact.getUserID()))
            holder.contact_selected_shadow.setVisibility(View.VISIBLE);
        else
            holder.contact_selected_shadow.setVisibility(View.GONE);

        holder.contact_for_new_group_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contacts_selected.contains(contact.getUserID()))
                    ((NewGroupActivity) mContext).addUserToSelection(holder.getAdapterPosition());
                else
                    ((NewGroupActivity) mContext).removeUserToSelection(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactsForGroupViewHolder extends RecyclerView.ViewHolder {

        LinearLayout contact_selected_shadow;
        LinearLayout contact_for_new_group_layout;
        TextView contact_name_first_part;
        TextView match_contact_name;
        TextView contact_name_second_part;
        TextView item_detail;
        ImageView contact_image;

        ContactsForGroupViewHolder(View itemView) {
            super(itemView);

            contact_selected_shadow = (LinearLayout) itemView.findViewById(R.id.contact_selected_shadow);
            contact_for_new_group_layout = (LinearLayout) itemView.findViewById(R.id.contact_for_new_group_layout);
            contact_name_first_part = (TextView) itemView.findViewById(R.id.group_name_first_part);
            match_contact_name = (TextView) itemView.findViewById(R.id.match_group_name);
            contact_name_second_part = (TextView) itemView.findViewById(R.id.group_name_second_part);
            item_detail = (TextView) itemView.findViewById(R.id.item_detail);
            contact_image = (ImageView) itemView.findViewById(R.id.contact_image);
        }
    }

    void setContacts_selected(ArrayList<Contact> contacts_selected_in) {
        contacts_selected.clear();
        for (Contact contact : contacts_selected_in)
            contacts_selected.add(contact.getUserID());
    }
}
