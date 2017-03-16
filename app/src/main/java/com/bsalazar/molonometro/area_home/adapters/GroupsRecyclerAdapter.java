package com.bsalazar.molonometro.area_home.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.DashboardGroupActivity;
import com.bsalazar.molonometro.area_home.MainActivity;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.Tools;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 28/02/2017.
 */

public class GroupsRecyclerAdapter extends RecyclerView.Adapter<GroupsRecyclerAdapter.GroupViewHolder> {

    private Context mContext;
    private ArrayList<Group> groups;

    public GroupsRecyclerAdapter(Context context, ArrayList<Group> groups) {
        this.mContext = context;
        this.groups = groups;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {

        Group group = groups.get(position);

        holder.group_name.setText(group.getName());

        try {
            if (group.getImageBase64() != null)
                try{
                    byte[] imageByteArray = Base64.decode(group.getImageBase64(), Base64.DEFAULT);

                    Glide.with(mContext)
                            .load(imageByteArray)
                            .asBitmap()
                            .into(holder.group_image);

                }catch (Exception e){
                    holder.group_image.setImageResource(R.drawable.user_icon);
                }
            else
                holder.group_image.setImageResource(R.drawable.group_icon);

        } catch (Exception e) {
            holder.group_image.setImageResource(R.drawable.group_icon);
        }

        holder.group_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, DashboardGroupActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {


        LinearLayout group_layout;
        TextView group_name;
        ImageView group_image;

        GroupViewHolder(View itemView) {
            super(itemView);

            group_layout = (LinearLayout) itemView.findViewById(R.id.group_layout);
            group_name = (TextView) itemView.findViewById(R.id.contact_name);
            group_image = (ImageView) itemView.findViewById(R.id.contact_image);
        }
    }
}
