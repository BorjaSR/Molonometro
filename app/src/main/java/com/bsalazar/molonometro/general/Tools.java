package com.bsalazar.molonometro.general;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Date;

/**
 * Created by bsalazar on 14/10/2016.
 */
public class Tools {

    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap) {

        Bitmap finalBitmap;
        if (bitmap.getWidth() >= bitmap.getHeight())
            finalBitmap = Bitmap.createBitmap(bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2, 0,
                    bitmap.getHeight(), bitmap.getHeight());

        else
            finalBitmap = Bitmap.createBitmap(bitmap,
                    0, bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(), bitmap.getWidth());

        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
                finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    public static String formatPhone(String old_phone){
        String new_phone = "NOT_FORMAT";
        if(old_phone != null){
            new_phone = old_phone;
            if(new_phone.length() == 9){
                new_phone = new_phone.substring(0,3) + " " + new_phone.substring(3, 6) + " " + new_phone.substring(6,9);
            }
        }

        return new_phone;
    }

    public static final double RESICED_CONSTAMT = 0.54;
    public static String encodeBitmapToBase64(Bitmap image) {

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

        image.compress(Bitmap.CompressFormat.JPEG, 25, baos2);
        int sinCompr = Base64.encodeToString(baos2.toByteArray(),Base64.DEFAULT).getBytes().length;
        if(sinCompr > 60000){

            double div = RESICED_CONSTAMT / (60000f / sinCompr);
            int division = (int) Math.ceil(div);
            if(division == 1) division = 2;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap resicedBitmap = getResizedBitmap(image, image.getWidth()/division, image.getHeight()/division);
            resicedBitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);

            return Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
        } else {
            return Base64.encodeToString(baos2.toByteArray(),Base64.DEFAULT);
        }

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String cropNameSurname(String Name){
        String[] split = Name.split(" ");
        if(split.length > 2){
            return split[0] + " " + split[1];
        } else
            return Name;
    }
    public static String cropName(String Name){
        return Name.split(" ")[0];
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

    public static boolean isToday(Date date){
        Date today = new Date();
        return today.getYear() == date.getYear() && today.getMonth() == date.getMonth() && today.getDay() == date.getDay();
    }
}
