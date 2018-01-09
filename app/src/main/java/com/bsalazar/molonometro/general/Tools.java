package com.bsalazar.molonometro.general;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bsalazar.molonometro.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class Tools {

    public static String formatPhone(String old_phone) {
        String new_phone = "NOT_FORMAT";
        if (old_phone != null) {
            new_phone = old_phone;
            if (new_phone.length() == 9) {
                new_phone = new_phone.substring(0, 3) + " " + new_phone.substring(3, 6) + " " + new_phone.substring(6, 9);
            }
        }

        return new_phone;
    }

    public static final double RESICED_CONSTAMT = 0.54;

    public static String encodeBitmapToBase64(Bitmap image) {

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 25, baos2);

        int sinCompr = Base64.encodeToString(baos2.toByteArray(), Base64.DEFAULT).getBytes().length;
        if (sinCompr > 60000) {

            double div = RESICED_CONSTAMT / (60000f / sinCompr);
            int division = (int) Math.ceil(div);
            if (division == 1) division = 2;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap resicedBitmap = getResizedBitmap(image, image.getWidth() / division, image.getHeight() / division);
            resicedBitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);

            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } else {
            return Base64.encodeToString(baos2.toByteArray(), Base64.DEFAULT);
        }

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String cropNameSurname(String Name) {
        if (Name == null)
            return "";

        String[] split = Name.split(" ");
        if (split.length > 2) {
            return split[0] + " " + split[1];
        } else
            return Name;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static int brightnessDown(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        // Get saturation and brightness.
        float[] hsbVals = new float[3];
        Color.RGBToHSV(r, g, b, hsbVals);

        if (hsbVals[2] - 0.15f > 0)
            hsbVals[2] = hsbVals[2] - 0.15f;
        else
            hsbVals[2] = 0.1f;

        return Color.HSVToColor(hsbVals);
    }

    public static int brightnessUp(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        // Get saturation and brightness.
        float[] hsbVals = new float[3];
        Color.RGBToHSV(r, g, b, hsbVals);

        if (hsbVals[2] + 0.15f < 1f)
            hsbVals[2] = hsbVals[2] + 0.15f;
        else
            hsbVals[2] = 0.9f;

        return Color.HSVToColor(hsbVals);
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private static String pathDev = "http://hostingtestbsalazar.esy.es/molonometro/imagesDEV/";
    private static String path = "http://hostingtestbsalazar.esy.es/molonometro/images/";

    public static String getUserImagePath(String fileName) {
        StringBuilder path = new StringBuilder();

        if (Constants.IS_DEVELOP)
            path.append(pathDev);
        else
            path.append(path);

        return path.append("Users/").append(fileName).append(".png").toString();
    }

    public static String getGroupImagePath(String fileName) {
        StringBuilder path = new StringBuilder();

        if (Constants.IS_DEVELOP)
            path.append(pathDev);
        else
            path.append(path);

        return path.append("Groups/").append(fileName).append(".png").toString();
    }

    public static String getCommentImagePath(String fileName) {
        StringBuilder path = new StringBuilder();

        if (Constants.IS_DEVELOP)
            path.append(pathDev);
        else
            path.append(path);

        return path.append("Comments/").append(fileName).append(".png").toString();
    }

    public static String formatDate(Context context, Date date){
        String dateString = "";

        SimpleDateFormat dateFormat;

        if (DateUtils.isToday(date.getTime())) {
            dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String dateFormatString = dateFormat.format(date);
            dateString = String.format(context.getString(R.string.today_date), dateFormatString);

        }else if (isYesterday(date.getTime())){
            dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String dateFormatString = dateFormat.format(date);
            dateString = String.format(context.getString(R.string.yesterday_date), dateFormatString);

        } else{
            dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateString = dateFormat.format(date);
        }

        return dateString;
    }
    public static String formatDateWithTime (Context context, Date date){
        String dateString = "";

        SimpleDateFormat dateFormat;

        if (DateUtils.isToday(date.getTime())) {
            dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String dateFormatString = dateFormat.format(date);
            dateString = String.format(context.getString(R.string.today_date), dateFormatString);

        }else if (isYesterday(date.getTime())){
            dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String dateFormatString = dateFormat.format(date);
            dateString = String.format(context.getString(R.string.yesterday_date), dateFormatString);

        } else{
            dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            dateString = dateFormat.format(date);
        }

        return dateString;
    }

    private static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE, -1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }
}
