package com.bsalazar.molonometro.area_dashboard_group.adapters;

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
import com.bsalazar.molonometro.area_dashboard_group.AddParticipantActivity;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Variables;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class AddParticipantAdapter extends RecyclerView.Adapter<AddParticipantAdapter.ContactsForGroupViewHolder> {

    private Context mContext;
    private ArrayList<Contact> contacts;

    public AddParticipantAdapter(Context context, ArrayList<Contact> contacts) {
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

        if (Variables.search_for_add_participant_to_group.equals("")) {
            holder.contact_name_first_part.setText(contact.getName());
            holder.contact_name_first_part.setVisibility(View.VISIBLE);
            holder.match_contact_name.setVisibility(View.GONE);
            holder.contact_name_second_part.setVisibility(View.GONE);

        } else {
            String[] parts = contact.getName().split(Variables.search_for_add_participant_to_group);
            if (parts.length == 0) {
                holder.match_contact_name.setText(Variables.search_for_add_participant_to_group);
                holder.contact_name_first_part.setVisibility(View.GONE);
                holder.contact_name_second_part.setVisibility(View.GONE);
            } else {
                holder.contact_name_first_part.setText(parts[0]);
                holder.match_contact_name.setText(Variables.search_for_add_participant_to_group);

                String second = "";
                for (int i = 1; i < parts.length; i++)
                    second += parts[i];
                holder.contact_name_second_part.setText(second);

                holder.match_contact_name.setVisibility(View.VISIBLE);
                holder.contact_name_second_part.setVisibility(View.VISIBLE);
            }
        }

        Glide.with(mContext)
                .load(contact.getImageURL())
                .asBitmap()
                .placeholder(R.drawable.user_icon)
                .into(holder.contact_image);

        if (isInGroup(contact)) {
            holder.contact_selected_shadow.setVisibility(View.VISIBLE);
            holder.item_detail.setText(mContext.getString(R.string.already_in_group));
            holder.contact_for_new_group_layout.setClickable(false);
        } else {
            holder.contact_selected_shadow.setVisibility(View.GONE);
            holder.item_detail.setText(contact.getState());
            holder.contact_for_new_group_layout.setClickable(true);

            holder.contact_for_new_group_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AddParticipantActivity) mContext).showAddParticipantDialog(contact);
                }
            });
        }


    }

    private boolean isInGroup(Contact contact) {
        for (Participant participant : Variables.Group.getParticipants())
            if (participant.getUserID() == contact.getUserID())
                return true;
        return false;
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
            contact_name_first_part = (TextView) itemView.findViewById(R.id.participant_name);
            match_contact_name = (TextView) itemView.findViewById(R.id.match_group_name);
            contact_name_second_part = (TextView) itemView.findViewById(R.id.group_name_second_part);
            item_detail = (TextView) itemView.findViewById(R.id.contact_state);
            contact_image = (ImageView) itemView.findViewById(R.id.contact_image);
        }
    }
}
