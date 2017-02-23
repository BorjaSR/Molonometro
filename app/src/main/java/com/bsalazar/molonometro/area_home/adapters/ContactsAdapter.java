package com.bsalazar.molonometro.area_home.adapters;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.MainActivity;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.general.Tools;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts = new ArrayList<>();
    private int resourceId;
    private Context mContext;

    private int contador = 0;

    HashMap<Integer, ImageView> imageViews = new HashMap<>();

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

        final Contact contact = contacts.get(position);

        TextView contact_name = (TextView) rootView.findViewById(R.id.contact_name);
        TextView item_detail = (TextView) rootView.findViewById(R.id.item_detail);
        TextView invite_contact_button = (TextView) rootView.findViewById(R.id.invite_contact_button);
        final ImageView contact_image = (ImageView) rootView.findViewById(R.id.contact_image);

        contact_name.setText(contact.getName());

        if (contact.isInApp()){

            item_detail.setText(contact.getState());
            try{
                byte[] imageByteArray = Base64.decode(contact.getImageBase64(), Base64.DEFAULT);

                imageViews.put(contact.getUserID(), contact_image);

//                Glide.with(mContext)
//                        .load(imageByteArray)
//                        .asBitmap()
//                        .dontAnimate()
//                        .listener(new RequestListener<byte[], Bitmap>() {
//                            @Override
//                            public boolean onException(Exception e, byte[] model, Target<Bitmap> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Bitmap resource, byte[] model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                contador++;
//                                Log.e("[RESOURCE READY]", " " + contador + " " + contact.getName());
//                                imageViews.get(contact.getUserID()).setImageBitmap(resource);
////                                contact_image.setImageBitmap(resource);
//                                return false;
//                            }
//                        })
//                .into(contact_image);

                contact_image.setImageBitmap(Tools.decodeBase64(contact.getImageBase64()));
            }catch (Exception e){
                contact_image.setImageResource(R.drawable.user_icon);
            }

            invite_contact_button.setVisibility(View.GONE);

        } else {
            item_detail.setText(Tools.formatPhone(contact.getPhone()));
            contact_image.setImageResource(R.drawable.user_icon);
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
