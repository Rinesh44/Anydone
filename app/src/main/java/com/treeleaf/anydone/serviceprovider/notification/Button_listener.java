package com.treeleaf.anydone.serviceprovider.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Button_listener extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.cancel(intent.getExtras().getInt("id"));
//        Toast.makeText(context, "Button clicked!", Toast.LENGTH_SHORT).show();


        String message = intent.getStringExtra("id");
        Toast.makeText(context, "ratnapark", Toast.LENGTH_SHORT).show();


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(intent.getExtras().getInt("id"));



    }
}