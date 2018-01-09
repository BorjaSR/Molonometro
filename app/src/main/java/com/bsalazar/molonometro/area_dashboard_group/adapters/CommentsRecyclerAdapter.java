package com.bsalazar.molonometro.area_dashboard_group.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
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
import com.bsalazar.molonometro.general.GetImageFromURL;
import com.bsalazar.molonometro.general.ImageActivity;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.PhotoDetailActivity;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.LikesController;
import com.bsalazar.molonometro.rest.services.ServiceCallback;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by bsalazar on 23/03/2017.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.CommentViewHolder> {

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
    public void onBindViewHolder(final CommentViewHolder holder, final int position) {

        if (position == 0) {
            holder.window_to_termometer.setVisibility(View.VISIBLE);
            holder.comment_container.setVisibility(View.GONE);
            holder.backgorund_comment_item.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
        } else {
            holder.window_to_termometer.setVisibility(View.GONE);
            holder.comment_container.setVisibility(View.VISIBLE);
            holder.backgorund_comment_item.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_));

            final Comment comment = comments.get(position - 1);

            holder.comment_from.setText(comment.getUserName());
            holder.comment_to.setText(comment.getDestinationUserName());

            holder.comment_date.setText(Tools.formatDateWithTime(mContext, comment.getDate()));
            holder.comment.setText(comment.getText());


            // IMAGE
            if (comment.getImage() == null || comment.getImage().length() == 0)
                holder.comment_image.setVisibility(View.GONE);
            else {
                holder.comment_image.setVisibility(View.VISIBLE);

                Glide.with(mContext)
                        .load(comment.getImage())
                        .asBitmap()
                        .listener(new MyRequestListener(mContext, holder.user_image))
                        .into(holder.comment_image);

                holder.comment_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new GetImageFromURL(mContext, comment.getImage(), new GetImageFromURL.OnImageDownloadListener() {
                            @Override
                            public void onFinish(Bitmap bitmap) {

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();

                                Bundle args = new Bundle();
//                                args.putString("image", comment.getImage());
                                args.putString("title", comment.getUserName());
                                args.putString("subtitle", Tools.formatDateWithTime(mContext, comment.getDate()));

                                Intent intent = new Intent(mContext, ImageActivity.class);
                                intent.putExtras(args);
                                intent.putExtra("imageBytes", byteArray);

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    ActivityOptions options = ActivityOptions
                                            .makeSceneTransitionAnimation((Activity) mContext, holder.comment_image, mContext.getString(R.string.image_transition));
                                    mContext.startActivity(intent, options.toBundle());
                                } else
                                    mContext.startActivity(intent);
                            }
                        }).execute();
                    }
                });
            }

            int number_comments = comment.getComments().size();
            int number_likes = comment.getLikes().size();

            if (number_comments > 0) {
                holder.comments.setText(number_comments + " comentarios");
                holder.comments.setVisibility(View.VISIBLE);
            } else
                holder.comments.setVisibility(View.GONE);

            if (number_likes > 0) {
                holder.likes.setText(number_likes + " Me gusta");
                holder.likes.setVisibility(View.VISIBLE);
            } else
                holder.likes.setVisibility(View.GONE);


            Glide.with(mContext)
                    .load(comment.getUserImage())
                    .asBitmap()
                    .listener(new MyRequestListener(mContext, holder.user_image))
                    .placeholder(R.drawable.user_icon)
                    .into(holder.user_image);


            holder.likes_and_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((DashboardGroupActivity) mContext).showCommentsDialog(comment.getCommentID(), false, comment.getLikes());
                }
            });

            holder.to_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashboardGroupActivity) mContext).showCommentsDialog(comment.getCommentID(), true, comment.getLikes());
                }
            });


            if (comment.getLikes().contains(Variables.User.getUserID())) {
                holder.icon_like.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.like_green));
                holder.text_like.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                holder.icon_like.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.like_gray));
                holder.text_like.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_more));

            }

            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!comment.getLikes().contains(Variables.User.getUserID()))
                        new LikesController().addLikeToComment(mContext, comment.getCommentID(), new ServiceCallback() {
                            @Override
                            public void onSuccess(Object result) {
                                comment.getLikes().add(Variables.User.getUserID());

                                TransitionManager.beginDelayedTransition(holder.comment_container);
                                holder.likes.setText(comment.getLikes().size() + " Me gusta");
                                holder.likes.setVisibility(View.VISIBLE);

                                if (comment.getLikes().contains(Variables.User.getUserID())) {
                                    holder.icon_like.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.like_green));
                                    holder.text_like.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                                } else {
                                    holder.icon_like.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.like_gray));
                                    holder.text_like.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_more));

                                }

                                ((DashboardGroupActivity) mContext).addLikePoint(comment.getDestinationUserID());
                            }
                        });
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return comments.size() + 1;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        LinearLayout window_to_termometer;
        LinearLayout backgorund_comment_item;
        LinearLayout comment_container;
        LinearLayout likes_and_comments;
        ImageView user_image;
        ImageView comment_image;
        TextView comment_from;
        TextView comment_to;
        TextView comment_date;
        TextView comment;
        TextView likes;
        TextView comments;

        ImageView icon_like;
        TextView text_like;
        LinearLayout to_comment;
        LinearLayout like;

        CommentViewHolder(View itemView) {
            super(itemView);

            window_to_termometer = (LinearLayout) itemView.findViewById(R.id.window_to_termometer);
            backgorund_comment_item = (LinearLayout) itemView.findViewById(R.id.backgorund_comment_item);
            comment_container = (LinearLayout) itemView.findViewById(R.id.comment_container);
            likes_and_comments = (LinearLayout) itemView.findViewById(R.id.likes_and_comments);
            user_image = (ImageView) itemView.findViewById(R.id.user_image);
            comment_image = (ImageView) itemView.findViewById(R.id.comment_image);
            comment_from = (TextView) itemView.findViewById(R.id.comment_from);
            comment_to = (TextView) itemView.findViewById(R.id.comment_to);
            comment_date = (TextView) itemView.findViewById(R.id.comment_date);
            comment = (TextView) itemView.findViewById(R.id.comment);
            likes = (TextView) itemView.findViewById(R.id.likes);
            comments = (TextView) itemView.findViewById(R.id.comments);

            to_comment = (LinearLayout) itemView.findViewById(R.id.reply);
            like = (LinearLayout) itemView.findViewById(R.id.like);

            text_like = (TextView) itemView.findViewById(R.id.text_like);
            icon_like = (ImageView) itemView.findViewById(R.id.icon_like);
        }
    }
}
