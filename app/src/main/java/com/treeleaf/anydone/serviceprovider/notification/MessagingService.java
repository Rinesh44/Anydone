package com.treeleaf.anydone.serviceprovider.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.videocallreceive.VideoCallHandleActivity;

import java.util.ArrayList;
import java.util.Map;

import static androidx.core.app.NotificationCompat.DEFAULT_ALL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_API_KEY;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_API_SECRET;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_BASE_URL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_BRODCAST_CALL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_ACCOUNT_TYPE;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_NAME;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_PROFILE_URL;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_PARTICIPANT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_ROOM_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_RTC_MESSAGE_ID;
import static com.treeleaf.januswebrtc.Const.SERVICE_PROVIDER_TYPE;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";
    String MESSAGING_CHANNEL = "messaging_channel";
    String UPDATE_CHANNEL = "update_channel";
    String SILENT_CHANNEL = "silent_channel";
    NotificationManager notificationManager;
    private PendingIntent contentIntent;
    ArrayList<String> employeeProfileUris = new ArrayList<>();
    String callees;
    String localAccountId;

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
                        String inboxId = jsonObject.get("inboxId");
                        Intent inboxIntent = new Intent(this, InboxDetailActivity.class);
                        inboxIntent.putExtra("inbox_id", inboxId);
                        inboxIntent.putExtra("notification", true);

                        contentIntent = PendingIntent.getActivity(this, 0, inboxIntent,
                                0);
                    }
                    break;

                case "TICKET_COMMENTED_NOTIFICATION":
                    Tickets ticket = TicketRepo.getInstance().getTicketByIndex(Long.parseLong(ticketId));

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

        String body = jsonObject.get("body");
        GlobalUtils.showLog(TAG, "body check: " + body);
        if (DetectHtml.isHtml(body)) body = Html.fromHtml(body).toString();
        GlobalUtils.showLog(TAG, "body check after: " + body);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
//                MESSAGING_CHANNEL)
//                .setContentTitle(jsonObject.get("title"))
//                .setContentText(body)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setColor(getResources().getColor(R.color.colorPrimary))
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setAutoCancel(true)
//                .setContentIntent(contentIntent)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setSmallIcon(R.drawable.logo_mark);
//
//        assert notificationManager != null;
//        notificationManager.notify(notificationId.hashCode(), notificationBuilder.build());

        CustomNotification();
    }

    public void CustomNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_call_notification);
//        String strtitle = "Custom notification title";
//        String strtext = "Custom notification text";
//        Intent intent = new Intent(this, MessagingService.class);
//        intent.putExtra("title", strtitle);
//        intent.putExtra("text", strtext);
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MESSAGING_CHANNEL)
                .setSmallIcon(R.drawable.google_icon)
                .setTicker("some ticker")
                .setAutoCancel(true)
//                .setContentIntent(pIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setDefaults(DEFAULT_ALL)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContent(remoteViews);
        Notification notification = builder.build();

        remoteViews.setTextViewText(R.id.tv_callee_name_not, "Some one");

        setListeners(remoteViews);
        notification.contentView = remoteViews;
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, notification);
    }

    public void setListeners(RemoteViews view) {


        int notification_id = (int) System.currentTimeMillis();

//        Intent button_intent = new Intent("button_click");
        Intent button_intent = new Intent(this, Button_listener.class);
        button_intent.putExtra("id", notification_id);
        PendingIntent button_pending_event = PendingIntent.getBroadcast(this, notification_id,
                button_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        view.setOnClickPendingIntent(R.id.btn_cancel_call, button_pending_event);


        Intent videoCallIntent = new Intent(this, VideoCallHandleActivity.class);
        videoCallIntent.putExtra(NOTIFICATION_BRODCAST_CALL, true);
        videoCallIntent.putExtra(NOTIFICATION_RTC_MESSAGE_ID, "rtc_messageid");
        videoCallIntent.putExtra(NOTIFICATION_BASE_URL, "https://mediaserver-mumbai-a.anydone.com/janus");
        videoCallIntent.putExtra(NOTIFICATION_API_KEY, "1234");
        videoCallIntent.putExtra(NOTIFICATION_API_SECRET, "A$#@2hsggsJHS0123GSA");
        videoCallIntent.putExtra(NOTIFICATION_ROOM_ID, "4662319310721360");
        videoCallIntent.putExtra(NOTIFICATION_PARTICIPANT_ID, "4662319310721360");
        videoCallIntent.putExtra(NOTIFICATION_CALLER_NAME, "Ramu Kaka");
        videoCallIntent.putExtra(NOTIFICATION_CALLER_ACCOUNT_ID, "ad3df69e946448b08b7c32b0b0656840");
        videoCallIntent.putExtra(NOTIFICATION_CALLER_PROFILE_URL, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Convex_lens_%28magnifying_glass%29_and_upside-down_image.jpg/341px-Convex_lens_%28magnifying_glass%29_and_upside-down_image.jpg");
        videoCallIntent.putExtra(NOTIFICATION_CALLER_ACCOUNT_TYPE, SERVICE_PROVIDER_TYPE);
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
        messagingChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(messagingChannel);

        NotificationChannel updateChannel = new NotificationChannel(UPDATE_CHANNEL,
                "Updates", NotificationManager.IMPORTANCE_DEFAULT);

        updateChannel.setDescription("Updates");
        updateChannel.enableLights(true);
        updateChannel.setLightColor(Color.WHITE);
        updateChannel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(updateChannel);

        NotificationChannel silentChannel = new NotificationChannel(SILENT_CHANNEL,
                "Silent", NotificationManager.IMPORTANCE_DEFAULT);

        silentChannel.setDescription("Silent");
        silentChannel.setImportance(NotificationManager.IMPORTANCE_LOW);
        silentChannel.enableLights(false);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(silentChannel);
    }
}
