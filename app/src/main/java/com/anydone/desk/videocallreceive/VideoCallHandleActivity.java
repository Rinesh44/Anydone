package com.anydone.desk.videocallreceive;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.protobuf.ByteString;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.NotificationProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.anydone.desk.R;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.inviteuserstocall.AddedParticipantsForCall;
import com.anydone.desk.inviteuserstocall.InviteUsersActivity;
import com.anydone.desk.mqtt.TreeleafMqttCallback;
import com.anydone.desk.mqtt.TreeleafMqttClient;
import com.anydone.desk.notification.ForegroundNotificationService;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.DialogUtils;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;
import com.treeleaf.januswebrtc.Callback;
import com.treeleaf.januswebrtc.ClientActivity;
import com.treeleaf.januswebrtc.Const;
import com.treeleaf.januswebrtc.Joinee;
import com.treeleaf.januswebrtc.RestChannel;
import com.treeleaf.januswebrtc.ServerActivity;
import com.treeleaf.januswebrtc.VideoCallUtil;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static com.treeleaf.anydone.entities.AnydoneProto.ServiceContext.INBOX_CONTEXT;
import static com.anydone.desk.utils.Constants.RTC_CONTEXT_INBOX;
import static com.anydone.desk.utils.Constants.RTC_CONTEXT_TICKET;
import static com.anydone.desk.utils.Constants.TOKEN;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_INVITE_BY_EMPLOYEE;
import static com.treeleaf.januswebrtc.Const.PUBLISHER;
import static com.treeleaf.januswebrtc.Const.JOINEE_LOCAL;
import static com.treeleaf.januswebrtc.Const.JOINEE_REMOTE;
import static com.treeleaf.januswebrtc.Const.MQTT_CONNECTED;
import static com.treeleaf.januswebrtc.Const.MQTT_DISCONNECTED;
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
import static com.treeleaf.januswebrtc.Const.SUBSCRIBER;
import static com.treeleaf.januswebrtc.ServerActivity.SERVER_ACTIVITY_REQ;

public class VideoCallHandleActivity extends MvpBaseActivity
        <VideoCallReceivePresenterImpl> implements
        VideoCallReceiveContract.VideoCallReceiveActivityView, TreeleafMqttClient.OnMQTTConnected {
    private static final String MQTT = "MQTT_EVENT_CHECK";
    private static final String TAG = "VideoReceiveActivity";
    private Callback.HostActivityCallback hostActivityCallbackServer;
    private Callback.HostActivityCallback hostActivityCallbackClient;

    private Account userAccount;
    private String janusBaseUrl, apiKey, apiSecret;
    private String serviceName;
    private ArrayList<String> serviceProfileUri;
    private String accountId, accountName, accountPicture, rtcMessageId;
    private ServerActivity.VideoCallListener videoCallListenerServer;
    private ClientActivity.VideoCallListener videoCallListenerClient;
    private AddedParticipantsReceiverCallback addedParticipantsReceiverCallback;
    private Callback.DrawPadEventListener drawPadEventListener;
    private Callback.DrawCallBack drawCallBack;
    String callerName;
    String callerAccountId;
    String callerProfileUrl;
    private long ticketId;
    private String refId;
    private boolean videoBroadCastPublish = false;
    int localDeviceWidth, localDeviceHeight;
    private Map<String, Integer[]> remoteDeviceResolutions = new HashMap<>();
    private String accountType;
    private Boolean isCallMultiple = true;
    private String fcmToken;
    private String mLocalParticipantId;
    private String mSessionId;
    private String mRoomId;
    public String rtcContext = RTC_CONTEXT_TICKET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        openActivityInLockedScreen();
        userAccount = AccountRepo.getInstance().getAccount();
        accountId = userAccount.getAccountId();
        accountName = userAccount.getFullName();
        accountPicture = userAccount.getProfilePic();

        TreeleafMqttClient.setOnMqttConnectedListener(this);
        //server callback
        hostActivityCallbackServer = new Callback.HostActivityCallback() {

            @Override
            public void fetchJanusServerInfo() {

            }

            @Override
            public void fetchCallerAndJanusCredentials(String mCallerContext) {
                Log.d("fcmtoken", "fetchCallerAndJanusCredentials:  " + fcmToken);

                if (mCallerContext.equals(RTC_CONTEXT_INBOX)) {
                    presenter.fetchCallerDetailsInbox(Hawk.get(Constants.TOKEN), fcmToken, accountId, mCallerContext);
                } else if (mCallerContext.equals(RTC_CONTEXT_TICKET)) {
                    presenter.fetchCallerDetailsTicket(Hawk.get(Constants.TOKEN), fcmToken, accountId, mCallerContext);
                }

            }

            @Override
            public void specifyRole(RestChannel.Role role) {
            }

            @Override
            public void passJanusServerInfo(BigInteger sessionId,
                                            BigInteger roomId, BigInteger participantId) {
                mSessionId = String.valueOf(sessionId);
                mRoomId = String.valueOf(roomId);
                mLocalParticipantId = String.valueOf(participantId);
            }

            @Override
            public void onServiceProviderAudioPublished(BigInteger sessionId, BigInteger roomId, BigInteger participantId) {

            }

            @Override
            public void passJoineeReceivedCallback(Callback.AudioVideoCallbackListener videoCallListener) {
                videoCallListenerServer = (ServerActivity.VideoCallListener) videoCallListener;
            }

            @Override
            public void passDrawPadEventListenerCallback(Callback.DrawPadEventListener drawPadEventListener) {
                VideoCallHandleActivity.this.drawPadEventListener = drawPadEventListener;
            }

            @Override
            public void notifyHostHangUp() {
            }

            @Override
            public void notifySubscriberLeft() {
                presenter.publishParticipantLeftEvent(accountId, accountName, accountPicture, refId, rtcContext, rtcMessageId);
            }

            @Override
            public void onPublisherVideoStarted() {
                presenter.publishSubscriberJoinEvent(accountId, accountName, accountPicture, refId, rtcContext, rtcMessageId);
            }

            @Override
            public String getLocalAccountId() {
                return accountId;
            }

            @Override
            public String getLocalAccountName() {
                return accountName;
            }

            @Override
            public String getCallerContext() {
                return rtcContext;
            }

            @Override
            public void unSubscribeVideoCallMqtt() {
                unSubscribeToMqttDrawing();
            }

            @Override
            public void notifyCallDecline() {
                presenter.publishCallDeclineEvent(accountId, accountName, accountPicture, refId, rtcContext, rtcMessageId);
            }

            @Override
            public void inviteUsersToCall() {
                startInviteUserActivity();
            }

            @Override
            public void sendDrawingViewResolution(int width, int height) {
                calculateTreeleafDrawPadViewResolution(width, height);
            }

            @Override
            public void closeAVCallNotification() {
                ForegroundNotificationService.removeCallNotification(getContext());
            }

            @Override
            public Boolean isProductionEnvironment() {
                String env = Hawk.get(Constants.BASE_URL);
                boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
                return prodEnv;
            }

        };

        //client callback
        hostActivityCallbackClient = new Callback.HostActivityCallback() {

            @Override
            public void fetchJanusServerInfo() {
                presenter.fetchJanusServerUrl(Hawk.get(Constants.TOKEN));
            }

            @Override
            public void fetchCallerAndJanusCredentials(String mCallerContext) {

            }

            @Override
            public void specifyRole(RestChannel.Role role) {
            }

            @Override
            public void onPublisherVideoStarted() {

            }

            @Override
            public String getLocalAccountId() {
                return accountId;
            }

            @Override
            public String getLocalAccountName() {
                return accountName;
            }

            @Override
            public String getCallerContext() {
                return rtcContext;
            }

            @Override
            public void unSubscribeVideoCallMqtt() {
                unSubscribeToMqttDrawing();
            }

            @Override
            public void notifyCallDecline() {
                presenter.publishCallDeclineEvent(accountId, accountName, accountPicture, refId, rtcContext, rtcMessageId);
            }

            @Override
            public void inviteUsersToCall() {
                startInviteUserActivity();
            }

            @Override
            public void sendDrawingViewResolution(int width, int height) {
                calculateTreeleafDrawPadViewResolution(width, height);
            }

            @Override
            public void passJanusServerInfo(BigInteger sessionId,
                                            BigInteger roomId, BigInteger participantId) {
                mSessionId = String.valueOf(sessionId);
                mRoomId = String.valueOf(roomId);
                mLocalParticipantId = String.valueOf(participantId);
                videoBroadCastPublish = true;
                presenter.publishVideoBroadCastMessage(accountId, accountName, accountPicture,
                        refId, String.valueOf(sessionId), String.valueOf(roomId),
                        String.valueOf(participantId), janusBaseUrl, apiSecret, apiKey, rtcContext);
            }

            @Override
            public void onServiceProviderAudioPublished(BigInteger sessionId, BigInteger roomId, BigInteger participantId) {

            }

            @Override
            public void passJoineeReceivedCallback(Callback.AudioVideoCallbackListener callback) {
                videoCallListenerClient = (ClientActivity.VideoCallListener) callback;
            }

            @Override
            public void passDrawPadEventListenerCallback(Callback.DrawPadEventListener drawPadEventListener) {
                VideoCallHandleActivity.this.drawPadEventListener = drawPadEventListener;
            }

            @Override
            public void notifyHostHangUp() {
                presenter.publishHostHangUpEvent(accountId, accountName, accountPicture,
                        refId, rtcMessageId, videoBroadCastPublish, rtcContext);
            }

            @Override
            public void notifySubscriberLeft() {

            }

            @Override
            public void closeAVCallNotification() {
                ForegroundNotificationService.removeCallNotification(getContext());
            }

            @Override
            public Boolean isProductionEnvironment() {
                String env = Hawk.get(Constants.BASE_URL);
                boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
                return prodEnv;
            }

        };

        drawCallBack = new Callback.DrawCallBack() {

            @Override
            public void onDiscardDraw(String imageId) {
                presenter.publishCancelDrawEvent(accountId, accountName, accountPicture, refId, System.currentTimeMillis(), rtcContext, imageId, rtcMessageId);
            }

            @Override
            public void onDrawCanvasCleared(String imageId) {
                presenter.publishDrawCanvasClearEvent(accountId, accountName, accountPicture, refId, System.currentTimeMillis(), rtcContext, imageId, rtcMessageId);
            }

            @Override
            public void onReceiveNewTextField(float x, float y, String editTextFieldId, String imageId, CaptureDrawParam captureDrawParam) {
                presenter.publishDrawReceiveNewTextEvent(accountId, accountName, accountPicture, x, y,
                        editTextFieldId, refId, System.currentTimeMillis(), rtcContext, imageId, rtcMessageId, captureDrawParam);
            }

            @Override
            public void onReceiveNewTextChange(String text, String id, String imageId) {
                presenter.publishTextFieldChangeEventEvent(accountId, accountName, accountPicture, text,
                        id, refId, System.currentTimeMillis(), rtcContext, imageId, rtcMessageId);
            }

            @Override
            public void onReceiveEdiTextRemove(String editTextId, String imageId) {
                presenter.publishTextFieldRemoveEventEvent(accountId, accountName, accountPicture, editTextId,
                        refId, System.currentTimeMillis(), rtcContext, imageId, rtcMessageId);
            }

            @Override
            public void onStartDraw(float x, float y, CaptureDrawParam captureDrawParam, String imageId, String touchSessionId) {
                presenter.publishDrawTouchDownEvent(accountId, accountName, accountPicture,
                        refId, x, y, captureDrawParam, System.currentTimeMillis(), rtcContext, imageId, touchSessionId, rtcMessageId);
            }

            @Override
            public void onClientTouchMove(CaptureDrawParam captureDrawParam, String imageId, Float prevX, Float prevY, String touchSessionId) {
                presenter.publishDrawTouchMoveEvent(accountId, accountName, accountPicture,
                        refId, captureDrawParam, prevX, prevY, System.currentTimeMillis(), rtcContext, imageId, touchSessionId, rtcMessageId);
            }

            @Override
            public void onClientTouchUp(String imageId, String touchSessionId) {
                presenter.publishDrawTouchUpEvent(accountId, accountName, accountPicture,
                        refId, System.currentTimeMillis(), rtcContext, imageId, touchSessionId, rtcMessageId);
            }

            @Override
            public void onCollabInvite(Joinee joinee, String pictureId, Bitmap bitmap) {
                prepareCollabInvite(joinee, pictureId, bitmap);
            }

            @Override
            public void onMaximizeDrawing(String pictureId) {
                presenter.publishDrawMaximize(accountId, pictureId, accountName, accountPicture,
                        refId, System.currentTimeMillis(), rtcContext, rtcMessageId);
            }

            @Override
            public void onMinimizeDrawing(String pictureId) {
                presenter.publishDrawMinimize(accountId, pictureId, accountName, accountPicture,
                        refId, System.currentTimeMillis(), rtcContext, rtcMessageId);
            }

            @Override
            public void onMaxDrawingExceed() {
                presenter.publishMaxDrawExceed(accountId, accountName, accountPicture,
                        refId, System.currentTimeMillis(), rtcContext, rtcMessageId);
            }

            @Override
            public void onPointerClicked(float x, float y, String imageId) {
                presenter.publishPointerClickEvent(accountId, accountName, accountPicture, x, y,
                        refId, System.currentTimeMillis(), rtcContext, imageId, rtcMessageId);
            }

        };

        addedParticipantsReceiverCallback = new AddedParticipantsReceiverCallback() {
            @Override
            public void sendAddedParticipants(ArrayList<AddedParticipantsForCall> addedParticipantsForCalls) {
                ArrayList<String> addedParticipantsIds = new ArrayList<>();
                for (AddedParticipantsForCall addedParticipantsForCall : addedParticipantsForCalls) {
                    addedParticipantsIds.add(addedParticipantsForCall.getAccountId());
                }
                presenter.publishAddParticipantsToCallMessage(accountId, accountName, accountPicture,
                        addedParticipantsIds, refId, mSessionId, mRoomId,
                        mLocalParticipantId, janusBaseUrl, apiSecret, apiKey, rtcContext, rtcMessageId);
            }
        };

        Boolean callTriggeredFromNotification = (Boolean) getIntent().getExtras().get(NOTIFICATION_BRODCAST_CALL);
        if (callTriggeredFromNotification != null && callTriggeredFromNotification && (!Const.CallStatus.isCallingScreenOn)) {
            fcmToken = (String) getIntent().getExtras().get(NOTIFICATION_TOKEN);

            //use this notification token to make api call


            String notRtcMessageId = (String) getIntent().getExtras().get(NOTIFICATION_RTC_MESSAGE_ID);
            String notBaseUrl = (String) getIntent().getExtras().get(NOTIFICATION_BASE_URL);
            String notApiKey = (String) getIntent().getExtras().get(NOTIFICATION_API_KEY);
            String notApiSecret = (String) getIntent().getExtras().get(NOTIFICATION_API_SECRET);
            String notRoomId = (String) getIntent().getExtras().get(NOTIFICATION_ROOM_ID);
            String notParticipantId = (String) getIntent().getExtras().get(NOTIFICATION_PARTICIPANT_ID);
            String notCallerName = (String) getIntent().getExtras().get(NOTIFICATION_CALLER_NAME);
            String notCallerAccountId = (String) getIntent().getExtras().get(NOTIFICATION_CALLER_ACCOUNT_ID);
            String notCallerProfileUrl = (String) getIntent().getExtras().get(NOTIFICATION_CALLER_PROFILE_URL);
            String notAccountType = (String) getIntent().getExtras().get(NOTIFICATION_CALLER_ACCOUNT_TYPE);

            String inviterAccountId = (String) getIntent().getExtras().get(NOTIFICATION_INVITE_BY_EMPLOYEE);
            Boolean isCallInvitation = (inviterAccountId != null);

            //if call is invited from employee, your call role is subscriber
            String localAccountType = isCallInvitation ? SUBSCRIBER : figureOutCallReceiverAccountType(notAccountType);

            String notNumberOfParticipants = (String) getIntent().getExtras().get(NOTIFICATION_NUMBER_OF_PARTICIPANTS);
            String referenceId = (String) getIntent().getExtras().get(NOTIFICATION_REFERENCE_ID);
            String callContext = (String) getIntent().getExtras().get(NOTIFICATION_CALLER_CONTEXT);
            Boolean directCallAccept = (Boolean) getIntent().getExtras().get(NOTIFICATION_DIRECT_CALL_ACCEPT);

            this.callerName = notCallerName;
            this.callerAccountId = notCallerAccountId;
            this.callerProfileUrl = notCallerProfileUrl;

            this.refId = referenceId;
            rtcMessageId = notRtcMessageId;
            this.rtcContext = callContext.equals("INBOX_CONTEXT") ? RTC_CONTEXT_INBOX : RTC_CONTEXT_TICKET;
            subscribeToMqttAVCall();
            subscribeToMqttDrawing();
            ForegroundNotificationService.removeCallNotification(this);
            ServerActivity.launchViaNotification(this, hostActivityCallbackServer, drawCallBack, notCallerName,
                    notCallerProfileUrl, notCallerAccountId, localAccountType, directCallAccept, true,
                    accountName, accountId, accountPicture, Integer.parseInt(notNumberOfParticipants) >= 3, isCallInvitation);
        }
    }

    private String figureOutCallReceiverAccountType(String callerAccountType) {
        switch (callerAccountType) {
            case "UNKNOWN_USER_TYPE":
            case "SERVICE_PROVIDER":
            case "EMPLOYEE":
            case "ANYDONE_USER":
            case "SERVICE_PROVIDER_CUSTOMER":
            case "ANYDONE_EMPLOYEE":
                return PUBLISHER;
            case "SERVICE_CONSUMER":
            case "EXTERNAL_CUSTOMER":
                return SUBSCRIBER;
        }
        return SUBSCRIBER;
    }

    private void startInviteUserActivity() {
        InviteUsersActivity.launch(VideoCallHandleActivity.this, addedParticipantsReceiverCallback, refId);
    }

    private void openActivityInLockedScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null)
                keyguardManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }

    private void calculateTreeleafDrawPadViewResolution(int width, int height) {
        localDeviceWidth = width;
        localDeviceHeight = height;
    }

    private void prepareCollabInvite(Joinee joinee, String pictureId, Bitmap caputureBitmap) {
        Bitmap bitmap = caputureBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap convertedBitmap;
        try {
            convertedBitmap = UiUtils.getResizedBitmap(bitmap, 800);
            byte[] bytes = GlobalUtils.bitmapToByteArray(convertedBitmap);
            ByteString imageByteString = ByteString.copyFrom(bytes);
            presenter.publishInviteToCollabRequest(accountId, joinee == null ? "ALL_PARTICIPANTS" : joinee.getAccountId(), pictureId, accountName,
                    accountPicture, refId, imageByteString, System.currentTimeMillis(), rtcContext, rtcMessageId);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Const.CallStatus.isCallingScreenOn = false;
        videoBroadCastPublish = false;//TODO: check if onresume gets called from child parent
    }

    @Override
    protected int getLayout() {
//        return 0;//TODO: fix this later
        return R.layout.link_layout;
    }


    //gets called from consumer
    public void setServiceName(String name) {
        this.serviceName = name;
    }

    //gets called from consumer
    public void setServiceProfileUri(ArrayList<String> uri) {
        this.serviceProfileUri = uri;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setReferenceId(String id) {
        refId = id;
    }

    public void setTicketRequestId(long ticketId) {
        this.ticketId = ticketId;
    }

    public void setIsCallMultiple(Boolean multipleCallers) {
        this.isCallMultiple = multipleCallers;
    }

    @Override
    public void onMqttReponseArrived(String mqttReponseType, boolean isLocalResponse) {
        if (videoCallListenerClient != null) {
            videoCallListenerClient.onMqttReponseArrived(mqttReponseType, isLocalResponse);
        }
        if (videoCallListenerServer != null) {
            videoCallListenerServer.onMqttReponseArrived(mqttReponseType, isLocalResponse);
        }
    }

    //video room initiation callback client
    @Override
    public void onVideoRoomInitiationSuccessClient(SignalingProto.BroadcastVideoCall broadcastVideoCall, AnydoneProto.ServiceContext context) {
        Log.d(MQTT, "onVideoRoomInitiationSuccess");
        rtcMessageId = broadcastVideoCall.getRtcMessageId();


        callerName = broadcastVideoCall.getSenderAccount().getFullName();
        callerAccountId = broadcastVideoCall.getSenderAccountId();
        callerProfileUrl = broadcastVideoCall.getSenderAccount().getProfilePic();

        /**
         * add caller/call initiator on the joinee list
         */
        videoCallListenerClient.onJoineeReceived(callerName, callerProfileUrl, callerAccountId, JOINEE_LOCAL);
    }

    @Override
    public void onImageDrawDiscardRemote(String accountId, String imageId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onImageReceivedFromConsumer");
                if (drawPadEventListener != null)
                    drawPadEventListener.onDrawDiscard(accountId, imageId);
            }
        });
    }

    public void onLocalVideoRoomJoinSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse) {
        Log.d(MQTT, "onVideoRoomJoinSuccess");
        if (videoCallListenerServer != null) {
            UserProto.Account account = videoCallJoinResponse.getSenderAccount();
            videoCallListenerServer.onJoineeReceived(account.getFullName(),
                    account.getProfilePic(), videoCallJoinResponse.getSenderAccountId(), JOINEE_LOCAL);

            /**
             * add caller/call initiator on the joinee list
             */
            videoCallListenerServer.onJoineeReceived(callerName, callerProfileUrl, callerAccountId, JOINEE_REMOTE);

            if (!Const.CallStatus.isCallTakingPlace)
                ForegroundNotificationService.removeCallNotification(this);
            videoCallListenerServer.checkCallHandledOnAnotherDevice();

        }
    }

    public void onRemoteVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse) {
        Log.d(MQTT, "onVideoRoomJoinSuccess");
        if (videoCallListenerClient != null) {
            UserProto.Account account = videoCallJoinResponse.getSenderAccount();
            videoCallListenerClient.onJoineeReceived(account.getFullName(),
                    account.getProfilePic(), videoCallJoinResponse.getSenderAccountId(), JOINEE_REMOTE);
        }
        if (videoCallListenerServer != null) {
            UserProto.Account account = videoCallJoinResponse.getSenderAccount();
            videoCallListenerServer.onJoineeReceived(account.getFullName(),
                    account.getProfilePic(), videoCallJoinResponse.getSenderAccountId(), JOINEE_REMOTE);
        }
    }

    public void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft) {
        Log.d(MQTT, "onParticipantLeft");
        if (videoCallListenerServer != null) {
            videoCallListenerServer.onJoineeRemoved(participantLeft.getSenderAccountId());
            videoCallListenerServer.onParticipantLeft();
        }
        if (videoCallListenerClient != null) {
            videoCallListenerClient.onJoineeRemoved(participantLeft.getSenderAccountId());
            videoCallListenerClient.onParticipantLeft();
        }
    }

    public void onHostHangUp(SignalingProto.VideoRoomHostLeft videoRoomHostLeft) {
        Log.d(MQTT, "onHostHangUp");
        if (videoCallListenerServer != null)
            videoCallListenerServer.onHostTerminateCall();
    }

    public void onCallDeclined(SignalingProto.ReceiverCallDeclined receiverCallDeclined) {
        Log.d(MQTT, "onHostHangUp");
        if (videoCallListenerClient != null)
            videoCallListenerClient.onCallDeclined();
    }

    @Override
    public void onDrawTouchDown(CaptureDrawParam captureDrawParam, String accountId, String imageId, String fullName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    if ((accountId != null && !accountId.isEmpty()) && (imageId != null && !imageId.isEmpty())) {
                        drawPadEventListener.onDrawParamChanged(captureDrawParam, accountId, imageId);
                        drawPadEventListener.onDrawNewDrawCoordinatesReceived(VideoCallUtil.normalizeXCoordinatePostPublish(captureDrawParam.getXCoordinate(),
                                localDeviceWidth), VideoCallUtil.normalizeYCoordinatePostPublish(captureDrawParam.getYCoordinate(),
                                localDeviceHeight), accountId, imageId);
                        drawPadEventListener.onDrawTouchDown(accountId, imageId, fullName);
                    } else {
                        Toast.makeText(VideoCallHandleActivity.this, "Draw start params missing", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onDrawTouchMove(CaptureDrawParam captureDrawParam, String accountId, String imageId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    drawPadEventListener.onDrawNewDrawCoordinatesReceived(VideoCallUtil.normalizeXCoordinatePostPublish(captureDrawParam.getXCoordinate(),
                            localDeviceWidth), VideoCallUtil.normalizeYCoordinatePostPublish(captureDrawParam.getYCoordinate(),
                            localDeviceHeight), accountId, imageId);
                    drawPadEventListener.onDrawTouchMove(accountId, imageId);
                }
            }
        });
    }

    @Override
    public void onDrawTouchUp(String accountId, String imageId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    if ((accountId != null && !accountId.isEmpty()) && (imageId != null && !imageId.isEmpty())) {
                        drawPadEventListener.onDrawTouchUp(accountId, imageId);
                    } else {
                        Toast.makeText(VideoCallHandleActivity.this, "Draw end params missing", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId,
                                          String imageId, CaptureDrawParam captureDrawParam) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    if ((editTextFieldId != null && !editTextFieldId.isEmpty()) &&
                            (accountId != null && !accountId.isEmpty()) && (imageId != null && !imageId.isEmpty())) {
                        drawPadEventListener.onDrawParamChanged(captureDrawParam, accountId, imageId);
                        drawPadEventListener.onDrawReceiveNewTextField(VideoCallUtil.normalizeXCoordinatePostPublish(x,
                                localDeviceWidth), VideoCallUtil.normalizeYCoordinatePostPublish(y,
                                localDeviceHeight), editTextFieldId, accountId, imageId);
                    } else
                        Toast.makeText(VideoCallHandleActivity.this, "New textfield params missing", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDrawReceiveNewTextChange(String text, String editTextFieldId, String accountId, String imageId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    if ((editTextFieldId != null && !editTextFieldId.isEmpty()) && (accountId != null && !accountId.isEmpty()) && (imageId != null && !imageId.isEmpty())) {
                        drawPadEventListener.onDrawReceiveNewTextChange(text, editTextFieldId, accountId, imageId);
                    } else
                        Toast.makeText(VideoCallHandleActivity.this, "Text change params missing", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDrawReceiveEdiTextRemove(String editTextFieldId, String accountId, String imageId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    if ((editTextFieldId != null && !editTextFieldId.isEmpty()) && (accountId != null && !accountId.isEmpty()) && (imageId != null && !imageId.isEmpty())) {
                        drawPadEventListener.onDrawReceiveEdiTextRemove(editTextFieldId, accountId, imageId);
                    } else
                        Toast.makeText(VideoCallHandleActivity.this, "Text remove params missing", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDrawPointerClicked(float x, float y, String accountId, String imageId, String fullName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    if ((accountId != null && !accountId.isEmpty()) && (imageId != null && !imageId.isEmpty())) {
                        drawPadEventListener.onDrawPointerClicked(VideoCallUtil.normalizeXCoordinatePostPublish(x,
                                localDeviceWidth), VideoCallUtil.normalizeYCoordinatePostPublish(y,
                                localDeviceHeight), accountId, imageId, fullName);
                    } else
                        Toast.makeText(VideoCallHandleActivity.this, "Pointer click params missing", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //onDrawParamChanged callback wont get called now
    @Override
    public void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId, String imageId) {
        if (drawPadEventListener != null) {
            drawPadEventListener.onDrawParamChanged(captureDrawParam, accountId, imageId);
        }
    }

    @Override
    public void onDrawCanvasCleared(String accountId, String imageId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    if ((accountId != null && !accountId.isEmpty()) && (imageId != null && !imageId.isEmpty()))
                        drawPadEventListener.onDrawCanvasCleared(accountId, imageId);
                    else
                        Toast.makeText(VideoCallHandleActivity.this, "Canvas clear params missing", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onDrawCollabInvite(SignalingProto.DrawCollab drawCollabResponse) {
        runOnUiThread(() -> {
            if (drawPadEventListener != null) {
                String fromAccountId = drawCollabResponse.getSenderAccountId();
                String toAccountId = drawCollabResponse.getToAccountId();
                String imageId = drawCollabResponse.getImageId();
                ByteString imageByteString = drawCollabResponse.getCapturedImage();
                if ((fromAccountId != null && !fromAccountId.isEmpty()) &&
                        (toAccountId != null && !toAccountId.isEmpty()) &&
                        (imageId != null && !imageId.isEmpty()) &&
                        (imageByteString != null)) {
                    byte[] convertedBytes = imageByteString.toByteArray();
                    drawPadEventListener.onDrawCollabInvite(fromAccountId, toAccountId, imageId, convertedBytes);
                } else {
                    Toast.makeText(VideoCallHandleActivity.this, "Draw collab params missing", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onDrawMaximize(SignalingProto.DrawMaximize drawMaximize) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    String fromAccountId = drawMaximize.getSenderAccountId();
                    String imageId = drawMaximize.getImageId();
                    drawPadEventListener.onDrawMaximize(fromAccountId, imageId);
                }
            }
        });
    }

    @Override
    public void onDrawMinimize(SignalingProto.DrawMinize drawMinize) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    String fromAccountId = drawMinize.getSenderAccountId();
                    String imageId = drawMinize.getImageId();
                    drawPadEventListener.onDrawMinimize(fromAccountId, imageId);
                }
            }
        });
    }

    @Override
    public void onDrawClose(SignalingProto.DrawClose drawClose) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    String fromAccountId = drawClose.getSenderAccountId();
                    String imageId = drawClose.getImageId();
                    drawPadEventListener.onDrawClose(fromAccountId, imageId);
                }
            }
        });
    }

    @Override
    public void onDrawMaxDrawingExceed(SignalingProto.MaxDrawingExceed maxDrawingExceed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    String fromAccountId = maxDrawingExceed.getSenderAccountId();
                    String fromAccountName = maxDrawingExceed.getSenderAccount().getFullName();
                    drawPadEventListener.onDrawMaxDrawingExceed(fromAccountId, fromAccountName);
                }
            }
        });
    }

    @Override
    public void onCallEndDetailsFetchSuccess(String callEndDetails) {

    }

    @Override
    public void onCallerDetailsFetchSuccess(NotificationProto.Notification notification) {
        if (videoCallListenerServer != null) {
            JSONObject payload = null;
            try {
                payload = new JSONObject(notification.getPayload());
                String notificationType = payload.optString("notificationType");

                JSONObject notificationPayloadJson = null;
                if (notificationType.equals("BROADCAST_VIDEO_CALL") || notificationType.equals("VIDEO_CALL")) {
                    notificationPayloadJson = payload.optJSONObject("broadcastVideoCall");
                } else if (notificationType.equals("ADD_CALL_PARTICIPANT")) {
                    notificationPayloadJson = payload.optJSONObject("addCallParticipant");
                }

                String roomId = notificationPayloadJson.optString("roomId");
                Log.d("callerroomnumber", "room number: " + roomId);
                String participantId = notificationPayloadJson.optString("participantId");

                JSONObject avConnectDetails = notificationPayloadJson.optJSONObject("avConnectDetails");
                janusBaseUrl = avConnectDetails.optString("baseUrl");
                apiKey = avConnectDetails.optString("apiKey");
                apiSecret = Hawk.get(TOKEN);
                videoCallListenerServer.onCallerDetailFetched(janusBaseUrl, apiKey, apiSecret, roomId, participantId);
            } catch (JSONException e) {
                e.printStackTrace();
                onCallerDetailFetchFail(e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void onCallerDetailFetchFail(String msg) {
        if (videoCallListenerServer != null)
            videoCallListenerServer.terminateCallReceive();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoCallHandleActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUrlFetchSuccess(String janusBaseUrl, String apiKey, String apiSecret) {
        this.janusBaseUrl = janusBaseUrl;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        Log.d(TAG, "janus server info: " + janusBaseUrl + apiKey + apiSecret);
        if (videoCallListenerClient != null) {
            videoCallListenerClient.onJanusCredentialsReceived(janusBaseUrl, apiKey,
                    Hawk.get(TOKEN), serviceName, serviceProfileUri, accountId);
        }

    }

    @Override
    public void onUrlFetchFail(String msg) {
        if (videoCallListenerClient != null)
            videoCallListenerClient.onJanusCredentialsFailure();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoCallHandleActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionSuccess() {
        if (serviceProfileUri.size() > 0) {
            subscribeToMqttDrawing();
            ClientActivity.launch(VideoCallHandleActivity.this,
                    false, hostActivityCallbackClient, drawCallBack,
                    serviceName, serviceProfileUri, accountType, accountPicture, isCallMultiple);//TODO: change it to SERVICE_PROVIDER_APP later
        } else {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        DialogUtils.Builder builder = new DialogUtils.Builder(getContext());
        DialogUtils dialogFragment = builder
                .setTitle("Info")
                .setMessage("You cannot make the call because ticket is requested and assigned to the same employee!")
                .setCanceleable(true)
                .setPositiveButtonTitle(getString(R.string.ok))
                .setNegativeButtonTitle(getString(R.string.cancel))
                .setDialogCallback(new AlertDialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {

                    }

                    @Override
                    public void onNegativeButtonClicked() {

                    }
                })
                .build();
        dialogFragment.show(getSupportFragmentManager(), TAG);
    }

    private void subscribeToMqttDrawing() {
        try {
            presenter.subscribeSuccessMessageDrawing(String.valueOf(refId), accountId);
            presenter.subscribeFailMessageDrawing(String.valueOf(refId), accountId);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void unSubscribeToMqttDrawing() {
        try {
            presenter.unSubscribeDrawing(String.valueOf(refId), accountId);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void subscribeToMqttAVCall() {
        try {
            presenter.subscribeSuccessMessageAVCall(refId, userAccount.getAccountId());
            presenter.subscribeFailMessageAVCall(refId);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void unSubscribeToMqttAVCall() {
        try {
            presenter.unSubscribeAVCall(refId, userAccount.getAccountId());
        } catch (MqttException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onConnectionFail(String msg) {
        /*try {
           //call method for unsubscribing here
        } catch (MqttException e) {
            e.printStackTrace();
        }*/

        Banner.make(Objects.requireNonNull(this).getWindow().getDecorView().getRootView(),
                this, Banner.INFO, msg, Banner.TOP, 3000).show();

        GlobalUtils.showLog(TAG, "mqtt not connected");
        String env = Hawk.get(Constants.BASE_URL);
        boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
        GlobalUtils.showLog(TAG, "prod env check: " + prodEnv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TreeleafMqttClient.start(
                    getApplicationContext(), prodEnv,
                    new TreeleafMqttCallback() {
                        @Override
                        public void messageArrived(String topic, MqttMessage message) {
                            GlobalUtils.showLog(TAG, "mqtt topic: " + topic);
                            GlobalUtils.showLog(TAG, "mqtt message: " + message);
                        }
                    });
        }

    }


    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    public void checkConnection(String accountType) {
        this.accountType = accountType;
        Const.CallStatus.isCallingScreenOn = true;
        presenter.checkConnection(TreeleafMqttClient.mqttClient);
    }

    @Override
    public void onMqttConnectionStatusChange(String connection) {
        if (connection.equals(MQTT_CONNECTED)) {
            if (videoCallListenerClient != null) {
                videoCallListenerClient.onMqttConnectionChanged(MQTT_CONNECTED);
            }
            if (videoCallListenerServer != null) {
                videoCallListenerServer.onMqttConnectionChanged(MQTT_CONNECTED);
            }
        } else {
            if (videoCallListenerClient != null) {
                videoCallListenerClient.onMqttConnectionChanged(MQTT_DISCONNECTED);
            }
            if (videoCallListenerServer != null) {
                videoCallListenerServer.onMqttConnectionChanged(MQTT_DISCONNECTED);
            }
        }
    }

    @Override
    public void mqttConnected() {
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.SUCCESS, "Connected", Banner.TOP, 2000).show();

    }

    @Override
    public void mqttNotConnected() {
        onConnectionFail("Reconnecting...");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SERVER_ACTIVITY_REQ:
                if (resultCode == RESULT_OK) {
                    String text = data.getStringExtra("key_finish_server_activity");
                    if (text.equals("server_activity_finish")) {
                        unSubscribeToMqttAVCall();
                        finish();
                    }
                }
                break;
        }
    }

    public interface AlertDialogCallback extends DialogUtils.DialogCallBack {
    }

    public interface AddedParticipantsReceiverCallback {
        void sendAddedParticipants(ArrayList<AddedParticipantsForCall> addedParticipantsForCalls);
    }

}
