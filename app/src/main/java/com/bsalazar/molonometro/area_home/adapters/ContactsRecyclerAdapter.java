package com.bsalazar.molonometro.area_home.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.ContactDetailActivity;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.GetImageFromURL;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.PhotoDetailActivity;
import com.bumptech.glide.Glide;
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder>
        implements SectionTitleProvider {

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
    public void onBindViewHolder(final ContactViewHolder holder, int position) {

        final Contact contact = contacts.get(position);

        holder.contact_name.setText(String.format(mContext.getString(R.string.user_name), contact.getUserName()));
        holder.item_detail.setText(contact.getName());

        Glide.with(mContext)
                .load(contact.getImageURL())
                .asBitmap()
                .listener(new MyRequestListener(mContext, holder.contact_image))
                .placeholder(R.drawable.user_icon)
                .into(holder.contact_image);

        holder.invite_contact_button.setVisibility(View.GONE);


        holder.contact_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContactDetailActivity.class);
                intent.putExtra("contactID", contact.getUserID());

                mContext.startActivity(intent);
            }
        });

        holder.contact_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (contact.getImageURL() != null) {
                    new GetImageFromURL(mContext, contact.getImageURL(), new GetImageFromURL.OnImageDownloadListener() {
                        @Override
                        public void onFinish(Bitmap bitmap) {

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();

                            // Supply index input as an argument.
                            Bundle args = new Bundle();
//                            args.putString("image", contact.getImageURL());
//                            args.putInt("noImage", R.drawable.user_icon);
                            args.putString("title", contact.getName());

                            Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                            intent.putExtras(args);
                            intent.putExtra("imageBytes", byteArray);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ActivityOptions options = ActivityOptions
                                        .makeSceneTransitionAnimation((MainActivity) mContext, holder.contact_image, mContext.getString(R.string.image_transition));
                                mContext.startActivity(intent, options.toBundle());
                            } else
                                mContext.startActivity(intent);

                        }
                    }).execute();
                }
            }
        });

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

    @Override
    public String getSectionTitle(int position) {
        return contacts.get(position).getName().substring(0, 1);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        LinearLayout contact_layout;
        ImageView contact_image;
        TextView contact_name;
        TextView item_detail;
        TextView invite_contact_button;

        ContactViewHolder(View itemView) {
            super(itemView);

            contact_layout = (LinearLayout) itemView.findViewById(R.id.contact_layout);
            contact_image = (ImageView) itemView.findViewById(R.id.contact_image);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            item_detail = (TextView) itemView.findViewById(R.id.contact_state);
            invite_contact_button = (TextView) itemView.findViewById(R.id.invite_contact_button);
        }
    }
}
