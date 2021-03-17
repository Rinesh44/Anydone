package com.treeleaf.anydone.serviceprovider.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.videocallreceive.VideoCallHandleActivity;

import static androidx.core.app.NotificationCompat.CATEGORY_CALL;
import static androidx.core.app.NotificationCompat.DEFAULT_ALL;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class ForegroundNotificationService extends Service {

    private static final String LOG_TAG = ForegroundNotificationService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void showNotification(Context context) {
        Intent intent = new Intent(context, ForegroundNotificationService.class);
        intent.setAction(Constants.ACTION.START_FOREGROUND_ACTION);
        context.startService(intent);
    }

    public static void removeNotification(Context context) {
        Intent intent = new Intent(context, ForegroundNotificationService.class);
        intent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.START_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            showNotification();
        } else if (intent.getAction().equals(
                Constants.ACTION.STOP_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private void showNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_call_notification);
        Intent notificationIntent = new Intent(this, VideoCallHandleActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel();
        else {
            channel = "";
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL.NC_ANYDONE_CALL)
                .setSmallIcon(R.drawable.google_icon)
                .setTicker("some ticker")
                .setContentText("Incoming call")
                .setAutoCancel(true)
                .setContentTitle("content title")
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
//                .setPriority(PRIORITY_HIGH)
                .setPriority(PRIORITY_MAX)
                .setCategory(CATEGORY_CALL)
//                .setContentIntent(pIntent)
                .setFullScreenIntent(pendingIntent,true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setDefaults(DEFAULT_ALL)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setVibrate(new long[]{500, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setContent(remoteViews);
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews);
        Notification notification = mBuilder.build();
        startForeground(352345235, notification);


        /**
         * startForeground() causes normal service transforms into a foreground service.
         * when a foreground service is stopped by calling the stopforeground() method, it does not stop the service, it juts removes
         * the service from the foreground mode.
         * To stop the service you may have to call the  stopSelf() method.
         * To start a service in foreground mode, you need to create an Android notification with notification id.
         */

    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String name = getString(R.string.app_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel callChannel = new NotificationChannel(Constants.CHANNEL.NC_ANYDONE_CALL, name, importance);
        callChannel.setDescription("AVCall");
        callChannel.enableLights(true);
        callChannel.setLightColor(Color.WHITE);
        callChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        callChannel.enableVibration(true);
        callChannel.setShowBadge(true);
        callChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        callChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(callChannel);
        } else {
            stopSelf();
        }
        return Constants.CHANNEL.NC_ANYDONE_CALL;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
