package com.bsalazar.molonometro.area_home.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.DashboardGroupActivity;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.GetImageFromURL;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.PhotoDetailActivity;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public void onBindViewHolder(final GroupViewHolder holder, int position) {

        final Group group = groups.get(position);

        holder.group_name.setText(group.getName());
        if (group.getLastEvent() != null) {
            String userName = group.getLastEvent().getUser().getUserName();
            String destinationUserName = group.getLastEvent().getDestinationUser().getUserName();

            holder.group_detail.setText(userName + " ha votado a " + destinationUserName);
        } else
            holder.group_detail.setText(mContext.getString(R.string.group_creation));

        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

        Date eventDate;
        if (group.getLastEvent() != null)
            eventDate = group.getLastEvent().

                    getLastUpdate();

        else
            eventDate = group.getLastUpdate();

        if (Tools.isToday(eventDate)) {
            holder.last_event_date.setText(formatTime.format(eventDate));
        } else
            holder.last_event_date.setText(formatDate.format(eventDate));

        Glide.with(mContext)
                .load(group.getImageURL())
                .asBitmap()
                .listener(new MyRequestListener(mContext, holder.group_image))
                .placeholder(R.drawable.group_icon)
                .into(holder.group_image);

        holder.group_image.setOnClickListener(
                new View.OnClickListener()

                {
                    @Override
                    public void onClick(View view) {
//
                        if (group.getImageURL() != null) {

                            new GetImageFromURL(mContext, group.getImageURL(), new GetImageFromURL.OnImageDownloadListener() {
                                @Override
                                public void onFinish(Bitmap bitmap) {

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byte[] byteArray = stream.toByteArray();

                                    // Supply index input as an argument.
                                    Bundle args = new Bundle();
//                                    args.putString("image", group.getImageURL());
//                                    args.putInt("noImage", R.drawable.group_icon);
                                    args.putString("title", group.getName());

                                    Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                                    intent.putExtras(args);
                                    intent.putExtra("imageBytes", byteArray);


                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                        ActivityOptions options = ActivityOptions
                                                .makeSceneTransitionAnimation((Activity) mContext, holder.group_image, mContext.getString(R.string.image_transition));
                                        mContext.startActivity(intent, options.toBundle());
                                    } else
                                        mContext.startActivity(intent);
                                }
                            }).execute();

                        }
                    }
                }

        );

        holder.group_layout.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Variables.Group = group;
                        new GroupController().getGroupParticipantsByID(group.getId(), new ServiceCallback() {
                            @Override
                            public void onSuccess(Object result) {
                                mContext.startActivity(new Intent(mContext, DashboardGroupActivity.class));
                            }

                            @Override
                            public void onFailure(String result) {

                            }
                        });
                    }
                }

        );
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
