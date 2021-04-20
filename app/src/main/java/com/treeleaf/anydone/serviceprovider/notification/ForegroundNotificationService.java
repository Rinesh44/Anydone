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
import android.media.AudioAttributes;
import android.net.Uri;
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

import java.util.Map;
import java.util.UUID;

import static androidx.core.app.NotificationCompat.CATEGORY_CALL;
import static androidx.core.app.NotificationCompat.DEFAULT_ALL;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;
import static com.treeleaf.anydone.serviceprovider.utils.Constants.NOTIFICATION_CLIENT_ID;
import static com.treeleaf.anydone.serviceprovider.utils.Constants.NOTIFICATION_LOCAL_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_API_KEY;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_API_SECRET;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_BASE_URL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_BRODCAST_CALL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_ACCOUNT_TYPE;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_CONTEXT;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_NAME;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_PROFILE_URL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_DIRECT_CALL_ACCEPT;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_NUMBER_OF_PARTICIPANTS;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_PARTICIPANT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_REFERENCE_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_ROOM_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_RTC_MESSAGE_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_TOKEN;

public class ForegroundNotificationService extends Service {

    private static final String LOG_TAG = ForegroundNotificationService.class.getSimpleName();
    public Uri soundUri;

    @Override
    public void onCreate() {
        super.onCreate();
        soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.call_incoming_new);
    }

    public static void showCallNotification(Context context, Map<String, String> jsonObject) {
        Intent intent = new Intent(context, ForegroundNotificationService.class);
        intent.setAction(Constants.ACTION.START_FOREGROUND_ACTION);
        intent = addExtras(intent, jsonObject);
        context.startService(intent);
    }

    private static Intent addExtras(Intent videoCallIntent, Map<String, String> jsonObject) {
        videoCallIntent.putExtra(NOTIFICATION_BRODCAST_CALL, true);
        videoCallIntent.putExtra(NOTIFICATION_RTC_MESSAGE_ID, jsonObject.get(NOTIFICATION_RTC_MESSAGE_ID));
        videoCallIntent.putExtra(NOTIFICATION_BASE_URL, jsonObject.get(NOTIFICATION_BASE_URL));
        videoCallIntent.putExtra(NOTIFICATION_API_KEY, jsonObject.get(NOTIFICATION_API_KEY));
        videoCallIntent.putExtra(NOTIFICATION_API_SECRET, jsonObject.get(NOTIFICATION_API_SECRET));
        videoCallIntent.putExtra(NOTIFICATION_ROOM_ID, jsonObject.get(NOTIFICATION_ROOM_ID));
        videoCallIntent.putExtra(NOTIFICATION_PARTICIPANT_ID, jsonObject.get(NOTIFICATION_PARTICIPANT_ID));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_NAME, jsonObject.get(NOTIFICATION_CALLER_NAME));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_ACCOUNT_ID, jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_ID));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_PROFILE_URL, jsonObject.get(NOTIFICATION_CALLER_PROFILE_URL));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_ACCOUNT_TYPE, jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_TYPE));
        videoCallIntent.putExtra(NOTIFICATION_TOKEN, jsonObject.get(NOTIFICATION_TOKEN));
        videoCallIntent.putExtra(NOTIFICATION_NUMBER_OF_PARTICIPANTS, jsonObject.get(NOTIFICATION_NUMBER_OF_PARTICIPANTS));
        videoCallIntent.putExtra(NOTIFICATION_REFERENCE_ID, jsonObject.get(NOTIFICATION_REFERENCE_ID));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_CONTEXT, jsonObject.get(NOTIFICATION_CALLER_CONTEXT));
        videoCallIntent.putExtra(NOTIFICATION_LOCAL_ACCOUNT_ID, jsonObject.get(NOTIFICATION_LOCAL_ACCOUNT_ID));
        videoCallIntent.putExtra(NOTIFICATION_DIRECT_CALL_ACCEPT, false);
        return videoCallIntent;
    }

    public static void removeCallNotification(Context context) {
        Intent intent = new Intent(context, ForegroundNotificationService.class);
        intent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.START_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            showCallNotification(intent);
        } else if (intent.getAction().equals(
                Constants.ACTION.STOP_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private void showCallNotification(Intent i) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_call_notification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel();
        Intent intent = createCallIntent(i, false);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL.NC_ANYDONE_CALL)
                .setSmallIcon(R.drawable.logo_mark)
//                .setTicker("some ticker")
//                .setContentText("Incoming call")
                .setAutoCancel(true)
//                .setContentTitle("content title")
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
//                .setPriority(PRIORITY_HIGH)
                .setPriority(PRIORITY_MAX)
                .setCategory(CATEGORY_CALL)
                .setContentIntent(pIntent)//had to add this in order to show call activity over lock screen
                .setFullScreenIntent(pIntent, true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setDefaults(DEFAULT_ALL)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setVibrate(new long[]{500, 1000})
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSound(soundUri)
//                .setContent(remoteViews)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_INSISTENT;

        remoteViews.setTextViewText(R.id.tv_callee_name_not, i.getStringExtra((NOTIFICATION_CALLER_NAME)));

        setListenersForCustomNotification(remoteViews, i);
        notification.contentView = remoteViews;

        startForeground(352345235, notification);


        /**
         * startForeground() causes normal service transforms into a foreground service.
         * when a foreground service is stopped by calling the stopforeground() method, it does not stop the service, it juts removes
         * the service from the foreground mode.
         * To stop the service you may have to call the  stopSelf() method.
         * To start a service in foreground mode, you need to create an Android notification with notification id.
         */

    }

    public void setListenersForCustomNotification(RemoteViews view, Intent i) {
        int notification_id = (int) System.currentTimeMillis();
        Intent declineCallIntent = createDeclineCallIntent(i);
        PendingIntent button_pending_event = PendingIntent.getBroadcast(this, notification_id,
                declineCallIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        view.setOnClickPendingIntent(R.id.btn_cancel_call, button_pending_event);

        Intent videoCallIntent = createCallIntent(i, true);
        PendingIntent pRadio = PendingIntent.getActivity(this, 0, videoCallIntent, PendingIntent.FLAG_ONE_SHOT);
        view.setOnClickPendingIntent(R.id.btn_accept_call, pRadio);
    }

    private Intent createCallIntent(Intent intent, boolean directCallAccept) {
        Intent videoCallIntent = new Intent(this, VideoCallHandleActivity.class);
        videoCallIntent.putExtra(NOTIFICATION_BRODCAST_CALL, true);
        videoCallIntent.putExtra(NOTIFICATION_RTC_MESSAGE_ID, intent.getStringExtra(NOTIFICATION_RTC_MESSAGE_ID));
        videoCallIntent.putExtra(NOTIFICATION_BASE_URL, intent.getStringExtra((NOTIFICATION_BASE_URL)));
        videoCallIntent.putExtra(NOTIFICATION_API_KEY, intent.getStringExtra((NOTIFICATION_API_KEY)));
        videoCallIntent.putExtra(NOTIFICATION_API_SECRET, intent.getStringExtra((NOTIFICATION_API_SECRET)));
        videoCallIntent.putExtra(NOTIFICATION_ROOM_ID, intent.getStringExtra((NOTIFICATION_ROOM_ID)));
        videoCallIntent.putExtra(NOTIFICATION_PARTICIPANT_ID, intent.getStringExtra((NOTIFICATION_PARTICIPANT_ID)));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_NAME, intent.getStringExtra((NOTIFICATION_CALLER_NAME)));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_ACCOUNT_ID, intent.getStringExtra((NOTIFICATION_CALLER_ACCOUNT_ID)));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_PROFILE_URL, intent.getStringExtra((NOTIFICATION_CALLER_PROFILE_URL)));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_ACCOUNT_TYPE, intent.getStringExtra((NOTIFICATION_CALLER_ACCOUNT_TYPE)));
        videoCallIntent.putExtra(NOTIFICATION_TOKEN, intent.getStringExtra((NOTIFICATION_TOKEN)));
        videoCallIntent.putExtra(NOTIFICATION_NUMBER_OF_PARTICIPANTS, intent.getStringExtra((NOTIFICATION_NUMBER_OF_PARTICIPANTS)));
        videoCallIntent.putExtra(NOTIFICATION_REFERENCE_ID, intent.getStringExtra((NOTIFICATION_REFERENCE_ID)));
        videoCallIntent.putExtra(NOTIFICATION_CALLER_CONTEXT, intent.getStringExtra((NOTIFICATION_CALLER_CONTEXT)));
        videoCallIntent.putExtra(NOTIFICATION_DIRECT_CALL_ACCEPT, directCallAccept);
        return videoCallIntent;
    }

    private Intent createDeclineCallIntent(Intent intent) {
        int notification_id = (int) System.currentTimeMillis();
        String clientId = UUID.randomUUID().toString().replace("-", "");
        Intent notificationDeclineIntent = new Intent(this, NotificationCancelListener.class);
        notificationDeclineIntent.putExtra(NOTIFICATION_LOCAL_ACCOUNT_ID, intent.getStringExtra(NOTIFICATION_LOCAL_ACCOUNT_ID));
        notificationDeclineIntent.putExtra(NOTIFICATION_REFERENCE_ID, intent.getStringExtra(NOTIFICATION_REFERENCE_ID));
        notificationDeclineIntent.putExtra(NOTIFICATION_CALLER_CONTEXT, intent.getStringExtra(NOTIFICATION_CALLER_CONTEXT));
        notificationDeclineIntent.putExtra(NOTIFICATION_CLIENT_ID, clientId);
        notificationDeclineIntent.putExtra("id", notification_id);
        return notificationDeclineIntent;
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
        callChannel.setSound(soundUri, getAudioAttributes());
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

    private AudioAttributes getAudioAttributes() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();

        return audioAttributes;
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
