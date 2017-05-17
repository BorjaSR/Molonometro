package com.bsalazar.molonometro.area_dashboard_group;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_dashboard_group.adapters.RepliesRecyclerAdapter;
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.CommentsController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by bsalazar on 10/05/2017.
 */

public class CommentsDialogFragment extends DialogFragment {

    private int commentID;

    private Context mContext;
    private LinearLayout reply_container;
    private EditText reply_text;
    private ImageView send_reply;
    private ProgressBar progress_replies;
    private RecyclerView commentsRecyclerView;
    private RepliesRecyclerAdapter adapter;

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.CommentsDialogAnimation;

        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.comments_dialog_fragment, container,
                false);

        commentID = getArguments().getInt("commentID");

        reply_container = (LinearLayout) rootView.findViewById(R.id.reply_container);
        reply_text = (EditText) rootView.findViewById(R.id.reply_text);
        progress_replies = (ProgressBar) rootView.findViewById(R.id.progress_replies);
        commentsRecyclerView = (RecyclerView) rootView.findViewById(R.id.reply_recycler);
        adapter = new RepliesRecyclerAdapter(mContext, new ArrayList<Comment>());
        send_reply = (ImageView) rootView.findViewById(R.id.send_reply);

        loadReplies();

        reply_text.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (s.length() > 0) {
                        TransitionManager.beginDelayedTransition(reply_container);

                        LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams) reply_text.getLayoutParams();
                        loparams.weight = 1;

                        reply_text.setLayoutParams(loparams);

                }else {
                    TransitionManager.beginDelayedTransition(reply_container);

                    LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams) reply_text.getLayoutParams();
                    loparams.weight = 0;

                    reply_text.setLayoutParams(loparams);
                }
            }
        });

        send_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reply_text.getText().toString().length() != 0) {
                    Comment reply = new Comment();
                    reply.setCommentID(commentID);
                    reply.setUserID(Variables.User.getUserID());
                    reply.setText(reply_text.getText().toString());

                    new CommentsController().addReplyToComment(mContext, reply, new ServiceCallbackInterface() {
                        @Override
                        public void onSuccess(String result) {
                            if (result.equals("true")) {
                                loadReplies();
                            }
                        }

                        @Override
                        public void onFailure(String result) {
                            Toast.makeText(getActivity(), "Oops! Ha ocurrido algún error", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else
                    Toast.makeText(getActivity(), "Pero no lo envies vacío animal!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void loadReplies() {
        new CommentsController().getRepliesByComment(mContext, commentID, new ServiceCallbackInterface() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jArray = new JSONArray(result);
                    ArrayList<Comment> replies = new ArrayList<>();

                    for (int i = 0; i < jArray.length(); i++)
                        replies.add(new Gson().fromJson(String.valueOf(jArray.getJSONObject(i)), Comment.class));
                    for (int i = 0; i < jArray.length(); i++)

                        progress_replies.setVisibility(View.GONE);
                    commentsRecyclerView.setVisibility(View.VISIBLE);
                    commentsRecyclerView.setHasFixedSize(false);
                    commentsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                    adapter = new RepliesRecyclerAdapter(mContext, replies);
                    commentsRecyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {

            }
        });
    }
}
