package com.example.labourondemand.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.labourondemand.R;

public class NotificationHelper {
    private static final String CHANNEl_ID = "ID";
    private static final String CHANNEL_NAME = "Name";
    private static final String CHANNEL_DESC = "DESC";

    private static Context mContext;
    public NotificationHelper(Context mContext) {
        this.mContext = mContext;
    }

    public static void displayNotification(Context context,String Title,String Body){

        mContext = context;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEl_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = (NotificationManager)mContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEl_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(Title)
                .setContentText(Body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

//        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
//        mNotificationManager.notify(1,mBuilder.build());
        Log.d("Notification he" +
                "lper",mContext+"");
        Log.d("Notification helper",context+"");
        Log.d("Notification helper",context.NOTIFICATION_SERVICE+"");
        NotificationManager notificationManager =(NotificationManager) mContext.getSystemService(context.NOTIFICATION_SERVICE);


        notificationManager.notify(1,mBuilder.build());

    }
}
