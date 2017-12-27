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

    private String imageURL;

    public SendFileFTP(String fileName, int mode, Bitmap bitmap, SendFileFTPListener onFinishListener) {
        this.fileName = fileName;
        this.mode = mode;
        this.imageByteArray = Base64.decode(Tools.encodeBitmapToBase64(bitmap), Base64.DEFAULT);
        this.onFinishListener = onFinishListener;
    }

    public SendFileFTP(String fileName, int mode, byte[] imageByteArray, SendFileFTPListener onFinishListener) {
        this.fileName = fileName;
        this.mode = mode;
        this.imageByteArray = imageByteArray;
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
                    imageURL = Tools.getUserImagePath(fileName);
                    break;
                case MODE_GROUP:
                    path.append("/Groups");
                    imageURL = Tools.getGroupImagePath(fileName);
                    break;
                case MODE_COMMENT:
                    path.append("/Comments");
                    imageURL = Tools.getCommentImagePath(fileName);
                    break;
            }

            ftpClient.changeWorkingDirectory(path.toString());
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ByteArrayInputStream bs = new ByteArrayInputStream(imageByteArray);
            ftpClient.enterLocalPassiveMode();
            ftpClient.storeFile(fileName + ".png", bs);

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
        if (onFinishListener != null) onFinishListener.onFinish(result, imageURL);
    }

    public void setOnFinishListener(SendFileFTPListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public interface SendFileFTPListener {
        void onFinish(boolean result, String URL);
    }
}
