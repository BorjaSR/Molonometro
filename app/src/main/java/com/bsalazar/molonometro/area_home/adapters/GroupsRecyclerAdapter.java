package com.bsalazar.molonometro.area_home.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.bsalazar.molonometro.general.PhotoDetailDialogFragment;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
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

        final Group group = groups.get(position);

        holder.group_name.setText(group.getName());

        try {
            if (group.getImageBase64() != null)
                try{
                    byte[] imageByteArray = Base64.decode(group.getImageBase64(), Base64.DEFAULT);

                    Glide.with(mContext)
                            .load(imageByteArray)
                            .asBitmap()
                            .listener(new MyRequestListener(mContext, holder.group_image))
                            .into(holder.group_image);

                }catch (Exception e){
                    holder.group_image.setImageResource(R.drawable.group_icon);
                }
            else
                holder.group_image.setImageResource(R.drawable.group_icon);

        } catch (Exception e) {
            holder.group_image.setImageResource(R.drawable.group_icon);
        }

        holder.group_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Variables.Group = group;
                PhotoDetailDialogFragment dialogSample = new PhotoDetailDialogFragment();

                // Supply index input as an argument.
                Bundle args = new Bundle();
                args.putString("image", group.getImageBase64());
                args.putInt("noImage", R.drawable.group_icon);
                dialogSample.setArguments(args);

                dialogSample.show(Constants.fragmentManager, "TAG");
            }
        });

        holder.group_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Variables.Group = group;
                new GroupController().getGroupParticipantsByID(mContext, group.getId(), new ServiceCallbackInterface() {
                    @Override
                    public void onSuccess(String result) {
                        mContext.startActivity(new Intent(mContext, DashboardGroupActivity.class));
                    }

                    @Override
                    public void onFailure(String result) {

                    }
                });
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
