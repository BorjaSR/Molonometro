package com.bsalazar.molonometro.area_home.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.DashboardGroupActivity;
import com.bsalazar.molonometro.area_home.MainActivity;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Tools;

import java.util.ArrayList;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class GroupsAdapter extends ArrayAdapter<Group> {

    private ArrayList<Group> groups = new ArrayList<>();
    private int resourceId;
    private Context mContext;

    public GroupsAdapter(Context context, int resource, ArrayList<Group> groups) {
        super(context, resource, groups);
        this.groups = groups;
        this.resourceId = resource;
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View rootView, ViewGroup parent) {

        if (rootView == null) {
            LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater1.inflate(resourceId, null);
        }

        Group group = groups.get(position);

        final LinearLayout group_layout = (LinearLayout) rootView.findViewById(R.id.group_layout);
        TextView group_name = (TextView) rootView.findViewById(R.id.contact_name);
        final ImageView group_image = (ImageView) rootView.findViewById(R.id.contact_image);

        group_name.setText(group.getName());

        try {
            Bitmap bitmap = Tools.decodeBase64(group.getImageBase64());
            if (bitmap != null)
                group_image.setImageBitmap(bitmap);
            else
                group_image.setImageResource(R.drawable.group_icon);

        } catch (Exception e) {
            group_image.setImageResource(R.drawable.group_icon);
        }

        group_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, DashboardGroupActivity.class));
            }
        });

        return rootView;
    }

}
