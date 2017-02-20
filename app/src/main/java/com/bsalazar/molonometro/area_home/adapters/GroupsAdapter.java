package com.bsalazar.molonometro.area_home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.MainActivity;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.Constants;

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

        if(rootView == null){
            LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater1.inflate(resourceId, null);
        }

        Group group = groups.get(position);

        final LinearLayout group_layout = (LinearLayout) rootView.findViewById(R.id.group_layout);
        TextView group_name = (TextView) rootView.findViewById(R.id.group_name);
        final ImageView group_image = (ImageView) rootView.findViewById(R.id.group_image);

        group_name.setText(group.getGroupName());
        group_image.setImageBitmap(group.getGroupImage());

        group_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).changeFragment(Constants.FRAG_ID_DASHBOARD_GROUP);
            }
        });

        return rootView;
    }

}
