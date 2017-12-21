package com.bsalazar.molonometro.area_adjust.adapters;

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
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.FriendRquest;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.UserController;
import com.bsalazar.molonometro.rest.json.RequestFriendJson;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.ContactsForGroupViewHolder> {

    private Context mContext;
    private ArrayList<FriendRquest> friendRquests;

    public PendingRequestAdapter(Context context, ArrayList<FriendRquest> friendRquests) {
        this.mContext = context;
        this.friendRquests = friendRquests;
    }

    @Override
    public ContactsForGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_contact_item, parent, false);
        return new ContactsForGroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ContactsForGroupViewHolder holder, int position) {
        final Contact contact = friendRquests.get(position).getContact();

        holder.participant_user_name.setText(String.format(mContext.getString(R.string.user_name), contact.getUserName()));
        holder.participant_name.setText(contact.getName());

        try {
            Glide.with(mContext)
                    .load(Base64.decode(contact.getImageBase64(), Base64.DEFAULT))
                    .asBitmap()
                    .listener(new MyRequestListener(mContext, holder.contact_image))
                    .into(holder.contact_image);

        } catch (Exception e) {
            holder.contact_image.setImageResource(R.drawable.user_icon);
        }

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestFriendJson requestFriendJson = new RequestFriendJson();
                requestFriendJson.setFriendID(Variables.User.getUserID());
                requestFriendJson.setUserID(contact.getUserID());
                new UserController().acceptFriendship(requestFriendJson, new ServiceCallback() {
                    @Override
                    public void onSuccess(String result) {
                        showRequestSend(holder);
                    }
                });
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showRequestSend(ContactsForGroupViewHolder holder){
        holder.buttons_container.setVisibility(View.GONE);
        holder.request_send.setText(mContext.getString(R.string.request_accepted));
        holder.request_send.setTextColor(mContext.getResources().getColor(R.color.gray));
        holder.request_send.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return friendRquests.size();
    }

    class ContactsForGroupViewHolder extends RecyclerView.ViewHolder {

        LinearLayout contact_for_new_group_layout;
        TextView participant_user_name, participant_name;
        ImageView contact_image;
        LinearLayout buttons_container;
        TextView accept, reject;
        TextView request_send;

        ContactsForGroupViewHolder(View itemView) {
            super(itemView);

            contact_for_new_group_layout = (LinearLayout) itemView.findViewById(R.id.contact_for_new_group_layout);
            participant_user_name = (TextView) itemView.findViewById(R.id.participant_user_name);
            participant_name = (TextView) itemView.findViewById(R.id.participant_name);
            contact_image = (ImageView) itemView.findViewById(R.id.contact_image);
            buttons_container = (LinearLayout) itemView.findViewById(R.id.buttons_container);
            accept = (TextView) itemView.findViewById(R.id.accept);
            reject = (TextView) itemView.findViewById(R.id.reject);
            request_send = (TextView) itemView.findViewById(R.id.request_send);
        }
    }
}
