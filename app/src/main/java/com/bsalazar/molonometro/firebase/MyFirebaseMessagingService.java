package com.bsalazar.molonometro.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.Constants;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.services.Parser;
import com.bsalazar.molonometro.rest.services.RestController;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bsalazar on 14/03/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private static final int NEW_GROUP_NOTIFICATION = 0;
    private static final int COMMENT_NOTIFICATION = 1;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (Constants.restController == null)
            Constants.restController = new RestController();

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            try {
                int tipo = (int) new JSONObject(remoteMessage.getData().toString()).get("tipo");

                switch (tipo) {
                    case NEW_GROUP_NOTIFICATION:
                        int groupID = (int) new JSONObject(remoteMessage.getData().toString()).get("GroupID");
                        GroupJson groupJson = new GroupJson();
                        groupJson.setGroupID(groupID);
                        new GroupController().getGroupByID(getApplicationContext(), groupJson, new ServiceCallbackInterface() {
                            @Override
                            public void onSuccess(String result) {

                                Group group = Parser.parseGroup(new Gson().fromJson(result, GroupJson.class));
                                Variables.groups.add(0, group);

                                FirebaseMessaging.getInstance().subscribeToTopic(group.getFirebaseTopic());

                                sendNotification(group.getName(), getString(R.string.added_to_group));
                            }

                            @Override
                            public void onFailure(String result) {

                            }
                        });
                        break;

                    case COMMENT_NOTIFICATION:
                        String json_comment = new JSONObject(remoteMessage.getData().toString()).get("comment").toString();
                        final Comment comment = new Gson().fromJson(json_comment, Comment.class);

                        GroupJson groupJson2 = new GroupJson();
                        groupJson2.setGroupID(comment.getGroupID());
                        new GroupController().getGroupByID(getApplicationContext(), groupJson2, new ServiceCallbackInterface() {
                            @Override
                            public void onSuccess(String result) {

                                GroupJson group = new Gson().fromJson(result, GroupJson.class);
                                String userName = null, destinationUserName = null;

                                for (Contact contact : Variables.contactsWithApp) {
                                    if (contact.getUserID() == comment.getUserID())
                                        userName = contact.getName();
                                    if (contact.getUserID() == comment.getDestinationUserID())
                                        destinationUserName = contact.getName();
                                }

                                boolean myself = false;
                                if (Variables.User != null) {
                                    if (comment.getUserID() == Variables.User.getUserID()) {
                                        userName = Variables.User.getName();
                                        myself = true;
                                    }
                                    if (comment.getDestinationUserID() == Variables.User.getUserID())
                                        destinationUserName = Variables.User.getName();
                                }

                                if (!myself)
                                    if (userName == null || destinationUserName == null)
                                        sendNotification(group.getName(), "Alguien ha votado!");
                                    else
                                        sendNotification(group.getName(), Tools.cropName(userName) + " ha votado a " + Tools.cropName(destinationUserName) + "!");

                            }

                            @Override
                            public void onFailure(String result) {

                            }
                        });

                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(Bitmap largeIcon, String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.shape)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
