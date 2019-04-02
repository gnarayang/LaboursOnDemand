package com.example.labourondemand.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getNotification() != null){
            String Title = remoteMessage.getNotification().getTitle();
            String Body = remoteMessage.getNotification().getBody();

            //NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
            NotificationHelper.displayNotification(getApplicationContext(),Title,Body);
        }
    }
}
