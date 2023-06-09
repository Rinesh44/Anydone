package com.anydone.desk.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.hawk.Hawk;
import com.anydone.desk.R;
import com.anydone.desk.inboxdetails.InboxDetailActivity;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.ticketdetails.TicketDetailsActivity;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.DetectHtml;
import com.anydone.desk.utils.ForegroundCheckTask;
import com.anydone.desk.utils.GlobalUtils;
import com.treeleaf.januswebrtc.Const;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_HOST_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_INVITE_BY_EMPLOYEE;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_LOCAL_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_TOKEN;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";
    private static final String NOTIFICATION_TAG = "NOTIFICATION_FIREBASE";
    String MESSAGING_CHANNEL = "messaging_channel";
    String UPDATE_CHANNEL = "update_channel";
    String SILENT_CHANNEL = "silent_channel";
    String AV_CALL_CHANNEL = "av_call_channel";
    NotificationManager notificationManager;
    private PendingIntent contentIntent;
    ArrayList<String> employeeProfileUris = new ArrayList<>();
    String callees;
    String localAccountId;
    boolean foregroud = false;
    private LocalBroadcastManager broadcastManager;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        GlobalUtils.showLog(TAG, "received notification msg data: " + remoteMessage.getData());
        GlobalUtils.showLog(TAG, "received notification msg noti: " + remoteMessage.getNotification());
        Account userAccount = AccountRepo.getInstance().getAccount();

        if (userAccount != null) {
            localAccountId = userAccount.getAccountId();

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel();
            }

            Map<String, String> jsonString = remoteMessage.getData();
            GlobalUtils.showLog(TAG, "jsonString: " + jsonString);

     /*       //update unreadMessageCount for inbox
            InboxRepo.getInstance().updateUnReadMessageCount(jsonString.get("inboxId"),
                    new Repo.Callback() {

                @Override
                public void success(Object o) {
                    GlobalUtils.showLog(TAG, "updated inbox unread message count");
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to update inbox unread message count");
                }

            });*/

            boolean loggedIn = Hawk.get(Constants.LOGGED_IN);
            if (loggedIn)
                setupNotification(jsonString);
        }
    }

    private void setupNotification(Map<String, String> jsonObject) {
        String ticketId = jsonObject.get("ticketId");
        String notificationId = jsonObject.get("notificationId");
        String senderId = jsonObject.get("senderAccountId");
        String type = jsonObject.get("type");

        if (type != null) {
            switch (type) {
                case "INBOX_NOTIFICATION":
                    if (!Const.CallStatus.isCallingScreenOn && jsonObject.get("inbox_notification_type") != null &&
                            jsonObject.get("inbox_notification_type").equals("VIDEO_CALL")
                            && !localAccountId.equals(jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_ID))) {
                        Log.d(NOTIFICATION_TAG, "incoming call from " + jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_ID));
                        if (jsonObject.get("notification_time_stamp_in_millis") != null) {
                            Long notificationTimeStampInMillis = Long.parseLong(jsonObject.get("notification_time_stamp_in_millis"));
                            if (!isNotificationStale(notificationTimeStampInMillis)) {
                                String fcmToken = jsonObject.get(NOTIFICATION_TOKEN);
                                Log.d("fcmtoken", "messagingservice:  " + fcmToken);
                                showForegroundNotification(jsonObject);
                            }
                        }
                    } else if (jsonObject.get("inbox_notification_type") != null &&
                            jsonObject.get("inbox_notification_type").equals("VIDEO_CALL_JOIN_REQUEST")
                    ) {
                        if (localAccountId.equals(jsonObject.get(NOTIFICATION_HOST_ACCOUNT_ID))) {
                            ForegroundNotificationService.removeCallNotification(this);
                        }
                        Log.d(NOTIFICATION_TAG, "join response from " + jsonObject.get(NOTIFICATION_HOST_ACCOUNT_ID));
                    } else if (jsonObject.get("inboxNotificationType") != null &&
                            jsonObject.get("inboxNotificationType").equals("VIDEO_ROOM_HOST_LEFT")
                    ) {
                        if (!localAccountId.equals(jsonObject.get(NOTIFICATION_HOST_ACCOUNT_ID))) {
                            ForegroundNotificationService.removeCallNotification(this);
                        }
                        Log.d(NOTIFICATION_TAG, "host left from " + jsonObject.get(NOTIFICATION_HOST_ACCOUNT_ID));
                    } else {
                        String inboxId = jsonObject.get("inboxId");
                        String sender = jsonObject.get("sender");

                        GlobalUtils.showLog(TAG, "user id: " + localAccountId);
                        GlobalUtils.showLog(TAG, "notification sender id: " + sender);

                        if (!localAccountId.equalsIgnoreCase(sender)) {
                            // send broadcast to increment notification count
                            Intent broadCastIntent = new Intent("broadcast_data");
                            broadCastIntent.putExtra("inbox_id", inboxId);
                            broadCastIntent.putExtra("sender", sender);
                            broadcastManager.sendBroadcast(broadCastIntent);
                        }


                        GlobalUtils.showLog(TAG, "inbox notification");
                        GlobalUtils.showLog(TAG, "inbox id check: " + inboxId);
                        Intent inboxIntent = new Intent(this, InboxDetailActivity.class);
                        inboxIntent.putExtra("inbox_id", inboxId);
                        inboxIntent.putExtra("notification", true);

                        contentIntent = PendingIntent.getActivity(this, 0, inboxIntent,
                                0);
                    }
                    break;

                case "TICKET_VIDEO_CALL_NOTIFICATION_TYPE":
                    if (!Const.CallStatus.isCallingScreenOn && jsonObject.get("ticket_video_call_notification_type") != null &&
                            jsonObject.get("ticket_video_call_notification_type").equals("BROADCAST_VIDEO_CALL")
                            && !localAccountId.equals(jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_ID))) {
                        Log.d(NOTIFICATION_TAG, "incoming call from " + jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_ID));
                        if (jsonObject.get("notification_time_stamp_in_millis") != null) {
                            Long notificationTimeStampInMillis = Long.parseLong(jsonObject.get("notification_time_stamp_in_millis"));
                            if (!isNotificationStale(notificationTimeStampInMillis)) {
                                String fcmToken = jsonObject.get(NOTIFICATION_TOKEN);
                                Log.d("fcmtoken", "messagingservice:  " + fcmToken);
                                showForegroundNotification(jsonObject);
                            }
                        }
                    } else if (!Const.CallStatus.isCallingScreenOn && jsonObject.get("ticket_video_call_notification_type") != null &&
                            jsonObject.get("ticket_video_call_notification_type").equals("ADD_CALL_PARTICIPANT")
                            && !localAccountId.equals(jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_ID))) {
                        Log.d(NOTIFICATION_TAG, "incoming call from " + jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_ID));
                        if (jsonObject.get("notification_time_stamp_in_millis") != null) {
                            Long notificationTimeStampInMillis = Long.parseLong(jsonObject.get("notification_time_stamp_in_millis"));
                            if (!isNotificationStale(notificationTimeStampInMillis)) {
                                String fcmToken = jsonObject.get(NOTIFICATION_TOKEN);
                                Log.d("fcmtoken", "messagingservice:  " + fcmToken);
                                jsonObject.put(NOTIFICATION_INVITE_BY_EMPLOYEE, jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_ID));
                                showForegroundNotification(jsonObject);
                            }
                        }
                    } else if (jsonObject.get("ticket_video_call_notification_type") != null &&
                            jsonObject.get("ticket_video_call_notification_type").equals("VIDEO_CALL_JOIN_RESPONSE")
                    ) {
                        if (localAccountId.equals(jsonObject.get(NOTIFICATION_HOST_ACCOUNT_ID)))
                            ForegroundNotificationService.removeCallNotification(this);
                        Log.d(NOTIFICATION_TAG, "join response from " + jsonObject.get(NOTIFICATION_HOST_ACCOUNT_ID));
                    } else if (jsonObject.get("ticket_video_call_notification_type") != null &&
                            jsonObject.get("ticket_video_call_notification_type").equals("VIDEO_ROOM_HOST_LEFT")
                    ) {
                        if (!localAccountId.equals(jsonObject.get(NOTIFICATION_HOST_ACCOUNT_ID)))
                            ForegroundNotificationService.removeCallNotification(this);
                        Log.d(NOTIFICATION_TAG, "host left from " + jsonObject.get(NOTIFICATION_HOST_ACCOUNT_ID));
                    }
                    break;
                case "TICKET_COMMENTED_NOTIFICATION":
                    if (ticketId != null) {
                        Tickets ticket = TicketRepo.getInstance().getTicketById(Long.parseLong(ticketId));

                        if (ticket != null) {
                            getRequiredDataFromTicket(ticket);

                            Intent ticketIntent = new Intent(this, TicketDetailsActivity.class);
                            ticketIntent.putExtra("selected_ticket_id", ticket.getTicketId());
                            ticketIntent.putExtra("selected_ticket_type", Constants.PENDING);
                            ticketIntent.putExtra("ticket_desc", ticket.getTitle());
                            ticketIntent.putExtra("selected_ticket_name", callees);
                            ticketIntent.putExtra("selected_ticket_index", ticket.getTicketIndex());
                            ticketIntent.putExtra("selected_ticket_status", ticket.getTicketStatus());
                            ticketIntent.putStringArrayListExtra("selected_ticket_icon_uri", employeeProfileUris);

                            contentIntent = PendingIntent.getActivity(this, 0, ticketIntent,
                                    0);
                        }


                        String body = jsonObject.get("body");
                        GlobalUtils.showLog(TAG, "body check: " + body);
                        if (DetectHtml.isHtml(body)) body = Html.fromHtml(body).toString();
                        GlobalUtils.showLog(TAG, "body check after: " + body);

                        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                R.drawable.logo_mark);

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                                MESSAGING_CHANNEL)
                                .setContentTitle(jsonObject.get("title"))
                                .setContentText(body)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setColor(getResources().getColor(R.color.colorPrimary))
                                .setAutoCancel(true)
                                .setContentIntent(contentIntent)
                                .setNumber(0)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(R.drawable.ic_create_new_grp);

                        assert notificationManager != null;
                        if (!(jsonObject.get("inbox_notification_type") != null && jsonObject.get("inbox_notification_type").equals("VIDEO_CALL")) &&
                                !(jsonObject.get("inboxNotificationType") != null && jsonObject.get("inboxNotificationType").equals("VIDEO_ROOM_HOST_LEFT"))) {
                            if (jsonObject.get("silent") != null) {
                                boolean isSilent = jsonObject.get("silent").equalsIgnoreCase("true");
                                try {
                                    foregroud = new ForegroundCheckTask().execute(getApplicationContext()).get();
                                    GlobalUtils.showLog(TAG, "foreground check: " + foregroud);
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (!isSilent && !foregroud)
                                    notificationManager.notify(notificationId.hashCode(), notificationBuilder.build());
                            } else {
                                if (!foregroud)
                                    notificationManager.notify(notificationId.hashCode(), notificationBuilder.build());
                            }
                        }
                        break;
                    }
            }
        }


    }

    private boolean isNotificationStale(Long notificationTimeStampInMillis) {
        long diffInMs = System.currentTimeMillis() - notificationTimeStampInMillis;
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
        return diffInSec >= 60;
    }

    private void showForegroundNotification(Map<String, String> jsonObject) {
        jsonObject.put(NOTIFICATION_LOCAL_ACCOUNT_ID, localAccountId);
        Handler handler = new Handler(Looper.getMainLooper());
        //show call notification for 40 seconds and cancel notification automatically after that.
        long delayInMilliseconds = 60000;
        handler.postDelayed(() -> ForegroundNotificationService
                .removeCallNotification(MessagingService.this), delayInMilliseconds);
        ForegroundNotificationService.showCallNotification(this, jsonObject);
    }

    private void getRequiredDataFromTicket(Tickets ticket) {
        StringBuilder builder = new StringBuilder();
        AssignEmployee assignedEmployee = ticket.getAssignedEmployee();
        String assignedEmployeeName = assignedEmployee.getName();

        Customer customer = ticket.getCustomer();
        String customerName = customer.getFullName();

        if (customer != null && !localAccountId.equals(customer.getCustomerId())
                && !customerName.isEmpty()) {
            builder.append(customerName);
            builder.append(", ");
            employeeProfileUris.add(customer.getProfilePic());
        }

        if (!localAccountId.equals(assignedEmployee.getAccountId()) &&
                assignedEmployeeName != null && !assignedEmployeeName.isEmpty()) {
            builder.append(assignedEmployeeName);
            builder.append(", ");
            employeeProfileUris.add(assignedEmployee.getEmployeeImageUrl());
        }

        for (AssignEmployee employee : ticket.getContributorList()) {
            if (!localAccountId.equals(employee.getAccountId())) {
                builder.append(employee.getName());
                builder.append(", ");
                employeeProfileUris.add(employee.getEmployeeImageUrl());
            }
        }

        String assignedEmployeeList = builder.toString().trim();
        callees = GlobalUtils.removeLastCharater(assignedEmployeeList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel messagingChannel = new NotificationChannel(MESSAGING_CHANNEL,
                "Messaging", NotificationManager.IMPORTANCE_HIGH);

        messagingChannel.setDescription("Messaging");
        messagingChannel.enableLights(true);
        messagingChannel.setLightColor(Color.WHITE);
        messagingChannel.setShowBadge(false);
        messagingChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        messagingChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(messagingChannel);

        NotificationChannel updateChannel = new NotificationChannel(UPDATE_CHANNEL,
                "Updates", NotificationManager.IMPORTANCE_HIGH);

        updateChannel.setDescription("Updates");
        updateChannel.enableLights(true);
        updateChannel.setLightColor(Color.WHITE);
        updateChannel.setShowBadge(true);
        updateChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        updateChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(updateChannel);

        NotificationChannel silentChannel = new NotificationChannel(SILENT_CHANNEL,
                "Silent", NotificationManager.IMPORTANCE_HIGH);

        silentChannel.setDescription("Silent");
        silentChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        silentChannel.enableLights(false);
        silentChannel.setShowBadge(true);
        silentChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(silentChannel);
    }
}
