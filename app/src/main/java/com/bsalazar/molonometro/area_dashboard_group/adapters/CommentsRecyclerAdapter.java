package com.bsalazar.molonometro.area_dashboard_group.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.adapters.GroupsRecyclerAdapter;
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.general.MyRequestListener;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentsRecyclerAdapter.CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final Comment comment = comments.get(position);

        holder.comment_from.setText(comment.getUserName());
        holder.comment_to.setText(comment.getDestinationUserName());
        holder.comment.setText(comment.getText());

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

        if(comment.isHasAnswers())
            holder.view_replies.setVisibility(View.VISIBLE);
        else
            holder.view_replies.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView comment_from;
        TextView comment_to;
        TextView comment;
        TextView view_replies;

        CommentViewHolder(View itemView) {
            super(itemView);

            user_image = (ImageView) itemView.findViewById(R.id.user_image);
            comment_from = (TextView) itemView.findViewById(R.id.comment_from);
            comment_to = (TextView) itemView.findViewById(R.id.comment_to);
            comment = (TextView) itemView.findViewById(R.id.comment);
            view_replies = (TextView) itemView.findViewById(R.id.view_replies);
        }
    }
}
