package com.bsalazar.molonometro.area_dashboard_group.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.DashboardGroupActivity;
import com.bsalazar.molonometro.area_home.adapters.GroupsRecyclerAdapter;
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.rest.controllers.CommentsController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import org.json.JSONArray;
import org.json.JSONException;

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
    public void onBindViewHolder(final CommentViewHolder holder, int position) {

        if(position == 0){
            holder.window_to_termometer.setVisibility(View.VISIBLE);
            holder.comment_container.setVisibility(View.GONE);
        } else {
            holder.window_to_termometer.setVisibility(View.GONE);
            holder.comment_container.setVisibility(View.VISIBLE);

            final Comment comment = comments.get(position - 1);

            holder.comment_from.setText(Tools.cropName(comment.getUserName()));
            holder.comment_to.setText(Tools.cropName(comment.getDestinationUserName()));
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
            else{
                holder.view_replies.setVisibility(View.GONE);
                holder.separator.setVisibility(View.GONE);
            }

            holder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashboardGroupActivity) mContext).showReplyDialog(comment);
                }
            });

            holder.view_replies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CommentsController().getRepliesByComment(mContext, comment.getCommentID(), new ServiceCallbackInterface() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONArray jArray = new JSONArray(result);
                                ArrayList<Comment> replies = new ArrayList<>();

                                for (int i = 0; i < jArray.length(); i++)
                                    replies.add(new Gson().fromJson(String.valueOf(jArray.getJSONObject(i)), Comment.class));

                                comment.setReplies(replies);
                                comment.setShowReplies(true);

                                notifyItemChanged(holder.getAdapterPosition());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(String result) {

                        }
                    });
                }
            });

            if(comment.getShowReplies()){
                holder.commentsRecyclerView.setVisibility(View.VISIBLE);
                holder.commentsRecyclerView.setHasFixedSize(false);
                holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                holder.adapter = new RepliesRecyclerAdapter(mContext, comment.getReplies());
                holder.commentsRecyclerView.setAdapter(holder.adapter);
            } else {
                holder.commentsRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return comments.size()+1;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        LinearLayout window_to_termometer;
        LinearLayout comment_container;
        ImageView user_image;
        TextView comment_from;
        TextView comment_to;
        TextView comment;

        TextView reply;
        LinearLayout separator;
        TextView view_replies;
        RecyclerView commentsRecyclerView;
        RepliesRecyclerAdapter adapter;

        CommentViewHolder(View itemView) {
            super(itemView);

            window_to_termometer = (LinearLayout) itemView.findViewById(R.id.window_to_termometer);
            comment_container = (LinearLayout) itemView.findViewById(R.id.comment_container);
            user_image = (ImageView) itemView.findViewById(R.id.user_image);
            comment_from = (TextView) itemView.findViewById(R.id.comment_from);
            comment_to = (TextView) itemView.findViewById(R.id.comment_to);
            comment = (TextView) itemView.findViewById(R.id.comment);

            commentsRecyclerView = (RecyclerView) itemView.findViewById(R.id.reply_recycler);
            reply = (TextView) itemView.findViewById(R.id.reply);
            separator = (LinearLayout) itemView.findViewById(R.id.separator);
            view_replies = (TextView) itemView.findViewById(R.id.view_replies);
            adapter = new RepliesRecyclerAdapter(mContext, new ArrayList<Comment>());
        }
    }
}
