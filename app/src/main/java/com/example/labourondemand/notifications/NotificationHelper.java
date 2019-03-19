package com.example.labourondemand.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEl_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = (NotificationManager)mContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEl_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(Title)
                .setContentText(Body)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

//        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
//        mNotificationManager.notify(1,mBuilder.build());

        NotificationManager notificationManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,mBuilder.build());

    }
}
