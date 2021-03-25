package com.treeleaf.anydone.serviceprovider.notification;

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
import android.text.Html;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.inboxdetails.InboxDetailActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.DetectHtml;
import com.treeleaf.anydone.serviceprovider.utils.ForegroundCheckTask;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.videocallreceive.VideoCallHandleActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static androidx.core.app.NotificationCompat.CATEGORY_CALL;
import static androidx.core.app.NotificationCompat.DEFAULT_ALL;
import static androidx.core.app.NotificationCompat.DEFAULT_SOUND;
import static androidx.core.app.NotificationCompat.DEFAULT_VIBRATE;
import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_API_KEY;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_API_SECRET;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_BASE_URL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_BRODCAST_CALL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_ACCOUNT_TYPE;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_NAME;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_PROFILE_URL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_DIRECT_CALL_ACCEPT;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_HOST_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_PARTICIPANT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_ROOM_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_RTC_MESSAGE_ID;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";
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

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        GlobalUtils.showLog(TAG, "received notification msg data: " + remoteMessage.getData());
        GlobalUtils.showLog(TAG, "received notification msg noti: " + remoteMessage.getNotification());
        Account userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        Map<String, String> jsonString = remoteMessage.getData();
        GlobalUtils.showLog(TAG, "jsonString: " + jsonString);
        setupNotification(jsonString);
    }

    private void setupNotification(Map<String, String> jsonObject) {
        String ticketId = jsonObject.get("ticketId");
        String notificationId = jsonObject.get("notificationId");
        String senderId = jsonObject.get("senderAccountId");
        String type = jsonObject.get("type");

        if (type != null) {
            switch (type) {
                case "INBOX_NOTIFICATION":
                    boolean loggedIn = Hawk.get(Constants.LOGGED_IN);
                    if (loggedIn) {
                        if (jsonObject.get("inboxNotificationType") != null &&
                                jsonObject.get("inboxNotificationType").equals("VIDEO_CALL")
                                && !localAccountId.equals(jsonObject.get(NOTIFICATION_CALLER_ACCOUNT_ID))) {
                            showForegroundNotification(jsonObject);
                        } else if (jsonObject.get("inboxNotificationType") != null &&
                                jsonObject.get("inboxNotificationType").equals("VIDEO_ROOM_HOST_LEFT")
                                && !localAccountId.equals(jsonObject.get(NOTIFICATION_HOST_ACCOUNT_ID))) {
                            ForegroundNotificationService.removeCallNotification(this);
                        } else {
                            String inboxId = jsonObject.get("inboxId");
                            GlobalUtils.showLog(TAG, "inbox notification");
                            GlobalUtils.showLog(TAG, "inbox id check: " + inboxId);
                            Intent inboxIntent = new Intent(this, InboxDetailActivity.class);
                            inboxIntent.putExtra("inbox_id", inboxId);
                            inboxIntent.putExtra("notification", true);

                            contentIntent = PendingIntent.getActivity(this, 0, inboxIntent,
                                    0);
                        }
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
                        break;
                    }
            }
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
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE) //Important for heads-up notification
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_create_new_grp);

        assert notificationManager != null;
        if (!(jsonObject.get("inboxNotificationType") != null && jsonObject.get("inboxNotificationType").equals("VIDEO_CALL")) &&
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

    }

    private void showForegroundNotification(Map<String, String> jsonObject) {
        ForegroundNotificationService.showCallNotification(this, jsonObject);
    }

    public void showCallNotification(Map<String, String> jsonObject) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_call_notification);
        Intent intent = createCallIntent(jsonObject, false);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AV_CALL_CHANNEL)
                .setSmallIcon(R.drawable.google_icon)
                .setTicker("some ticker")
                .setContentText("Incoming call")
                .setAutoCancel(true)
                .setContentTitle("content title")
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(PRIORITY_HIGH)
                .setCategory(CATEGORY_CALL)
//                .setContentIntent(pIntent)
                .setFullScreenIntent(pIntent, true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setDefaults(DEFAULT_ALL)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setVibrate(new long[]{500, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setContent(remoteViews);
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews);
        Notification notification = builder.build();

        remoteViews.setTextViewText(R.id.tv_callee_name_not, "Some one");

        setListenersForCustomNotification(remoteViews, jsonObject);
        notification.contentView = remoteViews;
        assert notificationManager != null;
        notificationManager.notify(0, notification);
    }

    private Intent createCallIntent(Map<String, String> jsonObject, Boolean directCallAccept) {
        Intent videoCallIntent = new Intent(this, VideoCallHandleActivity.class);
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
        videoCallIntent.putExtra(NOTIFICATION_DIRECT_CALL_ACCEPT, directCallAccept);
        return videoCallIntent;
    }

    public void setListenersForCustomNotification(RemoteViews view, Map<String, String> jsonObject) {
        int notification_id = (int) System.currentTimeMillis();
        Intent button_intent = new Intent(this, NotificationCancelListener.class);
        button_intent.putExtra("id", notification_id);
        PendingIntent button_pending_event = PendingIntent.getBroadcast(this, notification_id,
                button_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        view.setOnClickPendingIntent(R.id.btn_cancel_call, button_pending_event);

        Intent videoCallIntent = createCallIntent(jsonObject, true);
        PendingIntent pRadio = PendingIntent.getActivity(this, 0, videoCallIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_accept_call, pRadio);
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
        messagingChannel.setShowBadge(true);
        messagingChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        messagingChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(messagingChannel);

        NotificationChannel updateChannel = new NotificationChannel(UPDATE_CHANNEL,
                "Updates", NotificationManager.IMPORTANCE_HIGH);

        updateChannel.setDescription("Updates");
        updateChannel.enableLights(true);
        updateChannel.setLightColor(Color.WHITE);
        messagingChannel.setShowBadge(true);
        messagingChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        updateChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(updateChannel);

        NotificationChannel silentChannel = new NotificationChannel(SILENT_CHANNEL,
                "Silent", NotificationManager.IMPORTANCE_HIGH);

        silentChannel.setDescription("Silent");
        silentChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        silentChannel.enableLights(false);
        messagingChannel.setShowBadge(true);
        messagingChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(silentChannel);
    }
}
