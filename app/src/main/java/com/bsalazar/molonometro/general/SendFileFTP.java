package com.bsalazar.molonometro.general;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayInputStream;
import java.net.InetAddress;

/**
 * Created by bsalazar on 26/12/17.
 */

public class SendFileFTP extends AsyncTask<String, Void, Boolean> {

    private final String HOST = "hostingtestbsalazar.esy.es";
    private final String USER = "u274103892";
    private final String PASS = "hostingertestpass";
    private final String PATH = "/public_html/molonometro/images";
    private final String PATH_DEV = "/public_html/molonometro/imagesDEV";

    public static final int MODE_USER = 0;
    public static final int MODE_GROUP = 1;
    public static final int MODE_COMMENT = 2;

    private String fileName;
    private int mode;
    private byte[] imageByteArray;
    private SendFileFTPListener onFinishListener;

    public SendFileFTP(String fileName, int mode, Bitmap bitmap, SendFileFTPListener onFinishListener) {
        this.fileName = fileName + ".png";
        this.mode = mode;
        this.imageByteArray = Base64.decode(Tools.encodeBitmapToBase64(bitmap), Base64.DEFAULT);
        this.onFinishListener = onFinishListener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(InetAddress.getByName(HOST));
            ftpClient.login(USER, PASS);

            StringBuilder path = new StringBuilder();
            if(Constants.IS_DEVELOP)
                path.append(PATH_DEV);
            else
                path.append(PATH);

            switch (mode){
                case MODE_USER:
                    path.append("/Users");
                    break;
                case MODE_GROUP:
                    path.append("/Groups");
                    break;
                case MODE_COMMENT:
                    path.append("/Comments");
                    break;
            }

            ftpClient.changeWorkingDirectory(path.toString());
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ByteArrayInputStream bs = new ByteArrayInputStream(imageByteArray);
            ftpClient.enterLocalPassiveMode();
            ftpClient.storeFile(fileName, bs);

            ftpClient.logout();
            ftpClient.disconnect();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (onFinishListener != null) onFinishListener.onFinish(result);
    }

    public void setOnFinishListener(SendFileFTPListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public interface SendFileFTPListener {
        void onFinish(boolean result);
    }
}
