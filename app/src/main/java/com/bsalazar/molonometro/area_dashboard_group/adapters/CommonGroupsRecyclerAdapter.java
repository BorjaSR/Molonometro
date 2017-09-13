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
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 23/03/2017.
 */

public class CommonGroupsRecyclerAdapter extends RecyclerView.Adapter<CommonGroupsRecyclerAdapter.GroupViewHolder>  {

    private Context mContext;
    private ArrayList<Group> groups;

    public CommonGroupsRecyclerAdapter(Context context, ArrayList<Group> groups) {
        this.mContext = context;
        this.groups = groups;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new CommonGroupsRecyclerAdapter.GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder holder, int position) {
        Group group = groups.get(position);

        holder.group_name.setText(group.getName());

        try {
            if (group.getImageBase64() != null)
                try {
                    byte[] imageByteArray = Base64.decode(group.getImageBase64(), Base64.DEFAULT);

                    Glide.with(mContext)
                            .load(imageByteArray)
                            .asBitmap()
                            .listener(new MyRequestListener(mContext, holder.group_image))
                            .into(holder.group_image);

                } catch (Exception e) {
                    holder.group_image.setImageResource(R.drawable.group_icon);
                }
            else
                holder.group_image.setImageResource(R.drawable.group_icon);

        } catch (Exception e) {
            holder.group_image.setImageResource(R.drawable.group_icon);
        }

        holder.last_event_date.setVisibility(View.GONE);
        holder.group_detail.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        LinearLayout group_layout;
        TextView group_name;
        TextView group_detail;
        TextView last_event_date;
        ImageView group_image;

        GroupViewHolder(View itemView) {
            super(itemView);

            group_layout = (LinearLayout) itemView.findViewById(R.id.group_layout);
            group_name = (TextView) itemView.findViewById(R.id.group_name);
            group_detail = (TextView) itemView.findViewById(R.id.group_state);
            last_event_date = (TextView) itemView.findViewById(R.id.last_event_date);
            group_image = (ImageView) itemView.findViewById(R.id.group_image);
        }
    }
}
