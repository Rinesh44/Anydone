package com.treeleaf.anydone.serviceprovider.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationCancelListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.cancel(intent.getExtras().getInt("id"));//TODO: for some reason cancelling notification with id is not working
        ForegroundNotificationService.removeCallNotification(context);

    }
}
