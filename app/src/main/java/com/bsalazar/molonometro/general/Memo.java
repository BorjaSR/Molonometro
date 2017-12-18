package com.bsalazar.molonometro.general;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by rrodriguez on 06/10/2015.
 */
public class Memo {

    private static final String FILENAME_DEFAULT = "memo";
    public static final String NOT_SAVE = "NOT_SAVE";

    private static String getKey(Context context) {
        String a = "";
        try {
            a = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            Log.e("Memo: getKey(): A", e.getMessage());
        }
        String b = "";
        try {
            b = android.os.Build.HARDWARE;
        } catch (Exception e) {
            Log.e("Memo: getKey(): B", e.getMessage());
        }
        String c = "";
        try {
            c = android.os.Build.SERIAL;
        } catch (Exception e) {
            Log.e("Memo: getKey(): C", e.getMessage());
        }

        return a + ":" + b + ":" + c;
    }

    public static void forget(Context context) {
        forget(context, FILENAME_DEFAULT);
    }

    private static void forget(Context context, String filename) {
        rememberMe(context, filename, "");
    }

    public static void rememberMe(Context context, String me) {
        rememberMe(context, FILENAME_DEFAULT, me);
    }

    private static void rememberMe(Context context, String filename, String me) {

        String key = getKey(context);
        try {
            byte[] encrypt;
            Encryption encryption = new Encryption(key);
            encrypt = encryption.encrypt(me);

            if(encrypt != null)
                memoMemorize(context, filename, encrypt);

        } catch (Exception e) {
            // Cant remember
            Log.e("Memo: rememberMe()", e.getMessage());
        }
    }

    public static String doYouRemember(Context context) {
        return doYouRemember(context, FILENAME_DEFAULT);
    }

    private static String doYouRemember(Context context, String filename) {
        String me = null;

        String key = getKey(context);
        try {
            byte[] encrypt = memoRemember(context, filename);
            Encryption encryption = new Encryption(key);

            if(encrypt != null)
                me = encryption.decrypt(encrypt);

        } catch (Exception e) {
            // Cant remember
            Log.e("Memo: doYouRemember()", e.getMessage());
        }

        return me;
    }

    private static void memoMemorize(Context context, String filename, byte[] memo) {
        try {
            FileOutputStream outputStream  = context.openFileOutput(filename + ".txt", Context.MODE_PRIVATE);
            outputStream.write(memo);
            outputStream.close();
        } catch (Exception e) {
            // Cant save
            Log.e("Memo: memoMemorize()", e.getMessage());
        }
    }

    private static byte[] memoRemember(Context context, String filename) {
        byte[] memo = null;

        try {
            FileInputStream inputStream = context.openFileInput(filename + ".txt");
            memo = new byte[(int) inputStream.getChannel().size()];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(memo);
            inputStream.close();
        } catch (Exception e) {
            // Cant load
            Log.e("Memo: memoRemember()", e.getMessage());
        }

        return memo;
    }
}
