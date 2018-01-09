package com.bsalazar.molonometro.area_dashboard_group.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Tools;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by bsalazar on 23/03/2017.
 */

public class RepliesRecyclerAdapter extends RecyclerView.Adapter<RepliesRecyclerAdapter.ReplyViewHolder> {

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

        Glide.with(mContext)
                .load(comment.getUserImage())
                .asBitmap()
                .listener(new MyRequestListener(mContext, holder.user_image))
                .placeholder(R.drawable.user_icon)
                .into(holder.user_image);

        holder.comment_from.setText(comment.getUserName());
        holder.comment.setText(comment.getText());

        holder.reply_date.setText(Tools.formatDateWithTime(mContext, comment.getDate()));
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
