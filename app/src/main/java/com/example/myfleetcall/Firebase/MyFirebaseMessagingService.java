package com.example.myfleetcall.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.myfleetcall.R;
import com.example.myfleetcall.ReceiverObserver.CallLogObserver;
import com.example.myfleetcall.activity.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static String fcmToken = "";
    public static long startTime;
    public static boolean flagCallDetails = false;
    public static boolean flagCall = false;
    public static String CallTo = "";
    public static long currentTime;
    public static final String CHANNEL_1_ID = "channel1";
    private NotificationChannel mChannel;
    public static int notificationId = 1;
    NotificationManager notificationManager;
    public static String notiBody = null;
    private SharedPreferences.Editor mPreference;

    public static boolean FlagNoti = false;

    public MyFirebaseMessagingService() {

        super();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        //Log.d("Msg","Noti received"+remoteMessage.getNotification().getBody());
        //generateNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        flagCall = true;
        FlagNoti = true;


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> receivedMap = remoteMessage.getData();
            String title = receivedMap.get("title");
            String body = receivedMap.get("body");
            generateNotification(body, title);

            //get data
            mPreference = getApplicationContext().getSharedPreferences("MyFleetCall", MODE_PRIVATE).edit();
            mPreference.putBoolean("FlagNoti", true);
            mPreference.putString("NMobileNumber",body);
            mPreference.putString("CallTo", body);
            mPreference.apply();
            mPreference.commit();
        }
    }


    private void generateNotification(String body, String title) {


        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService
                    (Context.NOTIFICATION_SERVICE);
        }
        flagCall = true;
        int noti_id = 100;
        CallTo = body;
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);


        //intent.setData(Uri.parse("tel:" + body));
        CallTo = body;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setColor(Color.BLUE)
                //.addAction(R.drawable.ic_one,"CALL",pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setChannelId(CHANNEL_1_ID)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent);


        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationBuilder != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID,
                        "channel 1",
                        NotificationManager.IMPORTANCE_HIGH);
                /* Here is no constant like NotificationManager.IMPORTANCE_URGENT
                 * and i can't even put the another integer value, It not obeying the rules*/

                notificationManager.createNotificationChannel(channel);
                notificationBuilder.setChannelId(channel.getId());
                notificationBuilder.setAutoCancel(true);
            }
        }
        notificationManager.notify(notificationId, notificationBuilder.build());
        notificationBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        fcmToken = token;

    }

    public void getPhoneCallEndEvent() {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        CallLogObserver mObserver = new CallLogObserver(new Handler(), getApplicationContext());
        contentResolver.registerContentObserver(
                Uri.parse("content://call_log/calls"), true, mObserver);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(Context.ACCESSIBILITY_SERVICE);
        try {
            //on click notification

            Bundle extras = intent.getExtras();
            System.out.println("extras" + flagCall);
            if (true) {
                //Do your stuff here
                //get current time
                currentTime = System.currentTimeMillis();
                flagCallDetails = true;
                getPhoneCallEndEvent();
            }
        } catch (Exception e) {
            Log.e("onclick", "Exception onclick" + e);
        }
    }

//    private int getNotificationIcon() {
//        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
//        return useWhiteIcon ? R.drawable.ic_one : R.drawable.ic_one;
//    }


}



