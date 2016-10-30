package com.example.dopetheimmortal.turntapp.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.Activities.Home;
import com.example.dopetheimmortal.turntapp.R;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Dope The Immortal on 2016/10/29.
 */
public class GCMPushReceiverService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        System.out.println(data);
//        String message = data.getString("message");
//        Toast.makeText(this,"Ok "+data.getString("message"), Toast.LENGTH_LONG).show();
//
//        //Displaying a notiffication with the message
        sendNotification("You are being invited to "+data.getString("name"));
    }

    //This method is generating a notification and displaying the notificatokention
    private void sendNotification(String message) {
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setContentTitle("You have been invited to an event")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
    }
}




