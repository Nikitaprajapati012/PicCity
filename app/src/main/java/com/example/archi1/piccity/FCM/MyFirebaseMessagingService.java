package com.example.archi1.piccity.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.archi1.piccity.Activity.ChatActivity;
import com.example.archi1.piccity.Constant.Constant;
import com.example.archi1.piccity.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by archi on 10-Apr-17.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static String title;
    public String sender_id;
    public String recipient_id;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Constant.ON_CLICK_NOTIFICATION = 1;
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String jsonstring = remoteMessage.getData().toString();
            try {
                JSONObject mainObject = new JSONObject(jsonstring);
                title = mainObject.getString("push_message");
                sender_id = mainObject.getString("sender_id");
                recipient_id = mainObject.getString("recipient_id");
                Log.d("SNEDER_ID",""+sender_id);
                Log.d("RECEIPT_ID",""+recipient_id);
                Log.d("TITLE",""+ mainObject.getString("push_message"));
            } catch (JSONException e) {
                Log.d("Error", e.toString());
            }
            sendNotification(remoteMessage.getData().toString());


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("sender_id",sender_id);
        intent.putExtra("receipt_id",recipient_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("PicCity")
                .setContentText(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
