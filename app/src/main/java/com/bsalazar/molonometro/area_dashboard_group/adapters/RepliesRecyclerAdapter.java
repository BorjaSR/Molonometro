package com.bsalazar.molonometro.area_dashboard_group.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.DashboardGroupActivity;
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.rest.controllers.CommentsController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bsalazar on 23/03/2017.
 */

public class RepliesRecyclerAdapter extends RecyclerView.Adapter<RepliesRecyclerAdapter.ReplyViewHolder>  {

    private Context mContext;
    private ArrayList<Comment> comments;

    public RepliesRecyclerAdapter(Context context, ArrayList<Comment> comments) {
        this.mContext = context;
        this.comments = comments;
    }

    @Override
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, parent, false);
        return new RepliesRecyclerAdapter.ReplyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReplyViewHolder holder, int position) {

        final Comment comment = comments.get(position);

        try{
            byte[] imageByteArray = Base64.decode(comment.getUserImage(), Base64.DEFAULT);

            Glide.with(mContext)
                    .load(imageByteArray)
                    .asBitmap()
                    .listener(new MyRequestListener(mContext, holder.user_image))
                    .into(holder.user_image);

        }catch (Exception e){
            holder.user_image.setImageResource(R.drawable.user_icon);
        }

        holder.comment_from.setText(comment.getUserName());
        holder.comment.setText(comment.getText());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        final String date_string;
            if (Tools.isToday(comment.getDate()))
                date_string = mContext.getString(R.string.today) + " " + timeFormat.format(comment.getDate());
            else
                date_string = dateFormat.format(comment.getDate());

        holder.reply_date.setText(date_string);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ReplyViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView comment_from;
        TextView comment;
        TextView reply_date;

        ReplyViewHolder(View itemView) {
            super(itemView);

            user_image = (ImageView) itemView.findViewById(R.id.user_image);
            comment_from = (TextView) itemView.findViewById(R.id.comment_from);
            comment = (TextView) itemView.findViewById(R.id.comment);
            reply_date = (TextView) itemView.findViewById(R.id.reply_date);
        }
    }
}
