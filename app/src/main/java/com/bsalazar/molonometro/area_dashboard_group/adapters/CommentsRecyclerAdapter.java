package com.bsalazar.molonometro.area_dashboard_group.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Tools;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 23/03/2017.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.CommentViewHolder>  {

    private Context mContext;
    private ArrayList<Comment> comments;

    public CommentsRecyclerAdapter(Context context, ArrayList<Comment> comments) {
        this.mContext = context;
        this.comments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_v2, parent, false);
        return new CommentsRecyclerAdapter.CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {

        if(position == 0){
            holder.window_to_termometer.setVisibility(View.VISIBLE);
            holder.comment_container.setVisibility(View.GONE);
            holder.backgorund_comment_item.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
        } else {
            holder.window_to_termometer.setVisibility(View.GONE);
            holder.comment_container.setVisibility(View.VISIBLE);
            holder.backgorund_comment_item.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_));

            final Comment comment = comments.get(position - 1);

            holder.comment_from.setText(Tools.cropName(comment.getUserName()));
            holder.comment_to.setText(Tools.cropName(comment.getDestinationUserName()));
            holder.comment.setText(comment.getText());

            int number_comments = comment.getComments().size();
            int number_likes = comment.getLikes().size();

            if(number_comments > 0){
                holder.comments.setText(number_comments + " comentarios");
                holder.comments.setVisibility(View.VISIBLE);
            } else
                holder.comments.setVisibility(View.INVISIBLE);

            if(number_likes > 0){
                holder.likes.setText(number_likes + " Me gusta");
                holder.likes.setVisibility(View.VISIBLE);
            } else
                holder.likes.setVisibility(View.INVISIBLE);

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

            holder.likes_and_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((DashboardGroupActivity) mContext).showCommentsDialog(comment.getCommentID(), false);
                }
            });

            holder.to_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashboardGroupActivity) mContext).showCommentsDialog(comment.getCommentID(), true);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return comments.size()+1;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        LinearLayout window_to_termometer;
        LinearLayout backgorund_comment_item;
        LinearLayout comment_container;
        LinearLayout likes_and_comments;
        ImageView user_image;
        TextView comment_from;
        TextView comment_to;
        TextView comment;
        TextView likes;
        TextView comments;

        LinearLayout to_comment;

        CommentViewHolder(View itemView) {
            super(itemView);

            window_to_termometer = (LinearLayout) itemView.findViewById(R.id.window_to_termometer);
            backgorund_comment_item = (LinearLayout) itemView.findViewById(R.id.backgorund_comment_item);
            comment_container = (LinearLayout) itemView.findViewById(R.id.comment_container);
            likes_and_comments = (LinearLayout) itemView.findViewById(R.id.likes_and_comments);
            user_image = (ImageView) itemView.findViewById(R.id.user_image);
            comment_from = (TextView) itemView.findViewById(R.id.comment_from);
            comment_to = (TextView) itemView.findViewById(R.id.comment_to);
            comment = (TextView) itemView.findViewById(R.id.comment);
            likes = (TextView) itemView.findViewById(R.id.likes);
            comments = (TextView) itemView.findViewById(R.id.comments);

            to_comment = (LinearLayout) itemView.findViewById(R.id.view_replies);
        }
    }
}
