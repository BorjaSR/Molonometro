package com.bsalazar.molonometro.general;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

/**
 * Created by bsalazar on 26/12/17.
 */

public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {

    private Context mContext;
    private String imageURL;
    private OnImageDownloadListener onFinishListener;

    public GetImageFromURL(Context context, String imageURL, OnImageDownloadListener onFinishListener) {
        this.mContext = context;
        this.imageURL = imageURL;
        this.onFinishListener = onFinishListener;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        try {
            return Glide.with(mContext).
                    load(imageURL).
                    asBitmap().
                    into(100, 100). // Width and height
                    get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (onFinishListener != null) onFinishListener.onFinish(bitmap);
    }

    public interface OnImageDownloadListener {
        void onFinish(Bitmap bitmap);
    }
}
