package com.edu_touch.edu_hunt;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public  class MyFirebaseMessagingService extends FirebaseMessagingService {
    Activity _context;
    public SharedPreferences settings;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        int type=getSharedPreferences("login_info",MODE_PRIVATE).getInt("usertype",2);

        RemoteMessage.Notification data = remoteMessage.getNotification();

        Intent intent;
        if(type==2){
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }
        else {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 101, intent, 0);

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel = new NotificationChannel("222", "my_channel", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder =
                null
                ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder = new NotificationCompat.Builder(
                    getApplicationContext(), "222")
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.logo)
                    .setColor(Color.parseColor("#ffffff"))
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setContentIntent(pi);
        }

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        nm.notify(101, builder.build());
    }

}