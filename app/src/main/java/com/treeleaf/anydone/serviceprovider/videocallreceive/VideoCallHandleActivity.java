package com.treeleaf.anydone.serviceprovider.videocallreceive;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.protobuf.ByteString;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.januswebrtc.Callback;
import com.treeleaf.januswebrtc.ClientActivity;
import com.treeleaf.januswebrtc.Joinee;
import com.treeleaf.januswebrtc.RestChannel;
import com.treeleaf.januswebrtc.ServerActivity;
import com.treeleaf.januswebrtc.VideoCallUtil;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.treeleaf.anydone.serviceprovider.utils.Constants.RTC_CONTEXT_SERVICE_REQUEST;
import static com.treeleaf.januswebrtc.Const.JOINEE_LOCAL;
import static com.treeleaf.januswebrtc.Const.JOINEE_REMOTE;
import static com.treeleaf.januswebrtc.Const.MQTT_CONNECTED;
import static com.treeleaf.januswebrtc.Const.MQTT_DISCONNECTED;

public class VideoCallHandleActivity extends MvpBaseActivity
        <VideoCallReceivePresenterImpl> implements
        VideoCallReceiveContract.VideoCallReceiveActivityView, Callback.OnDrawEventListener {
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
    private Callback.DrawPadEventListener drawPadEventListener;
    private Callback.DrawCallBack drawCallBack;
    String callerName;
    String callerAccountId;
    String callerProfileUrl;
    private long refId, ticketId;
    private String rtcContext = RTC_CONTEXT_SERVICE_REQUEST;
    private boolean videoBroadCastPublish = false;
    int localDeviceWidth, localDeviceHeight;
    private Map<String, Integer[]> remoteDeviceResolutions = new HashMap<>();
    private boolean videoCallInitiated = false;
    private boolean videoReceiveInitiated = false;
    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAccount = AccountRepo.getInstance().getAccount();
        accountId = userAccount.getAccountId();
        accountName = userAccount.getFullName();
        accountPicture = userAccount.getProfilePic();
        localDeviceWidth = VideoCallUtil.getDeviceResolution(VideoCallHandleActivity.this)[0];
        localDeviceHeight = VideoCallUtil.getDeviceResolution(VideoCallHandleActivity.this)[1];

        //server callback
        hostActivityCallbackServer = new Callback.HostActivityCallback() {

            @Override
            public void fetchJanusServerInfo() {

            }

            @Override
            public void specifyRole(RestChannel.Role role) {
            }

            @Override
            public void passJanusServerInfo(BigInteger sessionId,
                                            BigInteger roomId, BigInteger participantId) {
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
                presenter.publishParticipantLeftEvent(accountId, accountName, accountPicture, refId, rtcContext);
            }

            @Override
            public void onPublisherVideoStarted() {
                presenter.publishSubscriberJoinEvent(accountId, accountName, accountPicture, refId, rtcContext);
            }

            @Override
            public String getLocalAccountId() {
                return accountId;
            }

        };

        //client callback
        hostActivityCallbackClient = new Callback.HostActivityCallback() {

            @Override
            public void fetchJanusServerInfo() {
                presenter.fetchJanusServerUrl(Hawk.get(Constants.TOKEN));
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
            public void passJanusServerInfo(BigInteger sessionId,
                                            BigInteger roomId, BigInteger participantId) {
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

        };

        drawCallBack = new Callback.DrawCallBack() {

            @Override
            public void onNewImageAcknowledge(int width, int height, long timeStamp) {
                presenter.publishSendAckToRemoteEvent(accountId, accountName, accountPicture, refId,
                        width, height, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onHoldDraw(String message) {
                if (drawPadEventListener != null)
                    drawPadEventListener.onDrawShowProgress(message);//TODO: uncomment this later
            }

            @Override
            public void onDiscardDraw(String imageId) {
                presenter.publishCancelDrawEvent(accountId, accountName, accountPicture, refId, System.currentTimeMillis(), rtcContext, imageId);
            }

            @Override
            public void onDrawCanvasCleared(String imageId) {
                presenter.publishDrawCanvasClearEvent(accountId, accountName, accountPicture, refId, System.currentTimeMillis(), rtcContext, imageId);
            }

            @Override
            public void onReceiveNewTextField(float x, float y, String editTextFieldId, String imageId) {
                presenter.publishDrawReceiveNewTextEvent(accountId, accountName, accountPicture, x, y,
                        editTextFieldId, refId, System.currentTimeMillis(), rtcContext, imageId);
            }

            @Override
            public void onReceiveNewTextChange(String text, String id, String imageId) {
                presenter.publishTextFieldChangeEventEvent(accountId, accountName, accountPicture, text,
                        id, refId, System.currentTimeMillis(), rtcContext, imageId);
            }

            @Override
            public void onReceiveEdiTextRemove(String editTextId, String imageId) {
                presenter.publishTextFieldRemoveEventEvent(accountId, accountName, accountPicture, editTextId,
                        refId, System.currentTimeMillis(), rtcContext, imageId);
            }

            @Override
            public void onDrawParamChanged(CaptureDrawParam captureDrawParam, String imageId) {
                presenter.publishDrawMetaChangeEvent(accountId, accountName, accountPicture, captureDrawParam.getXCoordinate(),
                        captureDrawParam.getYCoordinate(), captureDrawParam.getBrushWidth(),
                        Float.parseFloat(captureDrawParam.getBrushOpacity().toString()),
                        captureDrawParam.getBrushColor(), captureDrawParam.getTextColor(),
                        refId, System.currentTimeMillis(), rtcContext, imageId);
            }

            @Override
            public void onStartDraw(float x, float y, CaptureDrawParam captureDrawParam, String imageId) {
                presenter.publishDrawTouchDownEvent(accountId, accountName, accountPicture,
                        refId, x, y, captureDrawParam, System.currentTimeMillis(), rtcContext, imageId);
            }

            @Override
            public void onClientTouchMove(CaptureDrawParam captureDrawParam, String imageId) {
                presenter.publishDrawTouchMoveEvent(accountId, accountName, accountPicture,
                        refId, captureDrawParam.getXCoordinate(), captureDrawParam.getYCoordinate(),
                        System.currentTimeMillis(), rtcContext, imageId);
            }

            @Override
            public void onClientTouchUp(String imageId) {
                presenter.publishDrawTouchUpEvent(accountId, accountName, accountPicture,
                        refId, System.currentTimeMillis(), rtcContext, imageId);
            }

            @Override
            public void onNewImageFrameCaptured(Bitmap bitmap) {
                captureImageFrame(bitmap);
            }

            @Override
            public void onCollabInvite(Joinee joinee, String pictureId, Bitmap bitmap) {
                prepareCollabInvite(joinee, pictureId, bitmap);
            }

            @Override
            public void onMaximizeDrawing(String pictureId) {
                presenter.publishDrawMaximize(accountId, pictureId, accountName, accountPicture,
                        refId, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onMinimizeDrawing(String pictureId) {
                presenter.publishDrawMinimize(accountId, pictureId, accountName, accountPicture,
                        refId, System.currentTimeMillis(), rtcContext);
            }

        };
    }

    private void prepareCollabInvite(Joinee joinee, String pictureId, Bitmap caputureBitmap) {
        Bitmap bitmap = caputureBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap convertedBitmap;
        try {
            convertedBitmap = UiUtils.getResizedBitmap(bitmap, 400);
            byte[] bytes = GlobalUtils.bitmapToByteArray(convertedBitmap);
            ByteString imageByteString = ByteString.copyFrom(bytes);
            presenter.publishInviteToCollabRequest(accountId, joinee.getAccountId(), pictureId, accountName,
                    accountPicture, refId, imageByteString, System.currentTimeMillis(), rtcContext);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void captureImageFrame(Bitmap caputureBitmap) {
        Bitmap bitmap = caputureBitmap;
        Bitmap convertedBitmap;
        try {
            convertedBitmap = UiUtils.getResizedBitmap(bitmap, 400);
            byte[] bytes = GlobalUtils.bitmapToByteArray(convertedBitmap);
            ByteString imageByteString = ByteString.copyFrom(bytes);
            int localDeviceWidth = VideoCallUtil.getDeviceResolution(VideoCallHandleActivity.this)[0];
            int localDeviceHeight = VideoCallUtil.getDeviceResolution(VideoCallHandleActivity.this)[1];
            presenter.publishSendImageToRemoteEvent(accountId, accountName, accountPicture, refId, imageByteString,
                    localDeviceWidth, localDeviceHeight, System.currentTimeMillis(), rtcContext);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoCallInitiated = false;
        videoReceiveInitiated = false;
        videoBroadCastPublish = false;//TODO: check if onresume gets called from child parent
    }

    @Override
    protected int getLayout() {
        return 0;
    }


    //gets called from consumer
    public void setServiceName(String name) {
        this.serviceName = name;
    }

    //gets called from consumer
    public void setServiceProfileUri(ArrayList<String> uri) {
        this.serviceProfileUri = uri;
    }

    public void setReferenceId(long id) {
        refId = id;
    }

    public void setTicketRequestId(long ticketId) {
        this.ticketId = ticketId;
    }

    public void setRtcContext(String context) {
        this.rtcContext = context;
    }

    //video room initiation callback client
    public void onVideoRoomInitiationSuccessClient(SignalingProto.BroadcastVideoCall broadcastVideoCall) {
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

    // video room initiation callback server
    public void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                             boolean videoBroadcastPublish) {
        Log.d(MQTT, "onVideoRoomInitiationSuccess");
        if (!videoCallInitiated && !videoReceiveInitiated) {
            rtcMessageId = broadcastVideoCall.getRtcMessageId();
            String janusServerUrl = broadcastVideoCall.getAvConnectDetails().getBaseUrl();
            String janusApiKey = broadcastVideoCall.getAvConnectDetails().getApiKey();
            String janusApiSecret = broadcastVideoCall.getAvConnectDetails().getApiSecret();
            String roomNumber = broadcastVideoCall.getRoomId();
            String participantId = broadcastVideoCall.getParticipantId();

            callerName = broadcastVideoCall.getSenderAccount().getFullName();
            callerAccountId = broadcastVideoCall.getSenderAccountId();
            callerProfileUrl = broadcastVideoCall.getSenderAccount().getProfilePic();
            videoReceiveInitiated = true;
            ServerActivity.launch(this, janusServerUrl, janusApiKey, janusApiSecret,
                    roomNumber, participantId, hostActivityCallbackServer, drawCallBack, callerName, callerProfileUrl);
        }


    }

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

    public void onImageDrawDiscardLocal() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null)
                    drawPadEventListener.onDrawHideProgress();
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

    @Override
    public void onDrawTouchDown(CaptureDrawParam captureDrawParam, String accountId, String imageId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    if ((accountId != null && !accountId.isEmpty()) && (imageId != null && !imageId.isEmpty())) {
                        drawPadEventListener.onDrawParamChanged(captureDrawParam, accountId, imageId);
                        drawPadEventListener.onDrawNewDrawCoordinatesReceived(VideoCallUtil.normalizeXCoordinatePostPublish(captureDrawParam.getXCoordinate(),
                                localDeviceWidth), VideoCallUtil.normalizeYCoordinatePostPublish(captureDrawParam.getYCoordinate(),
                                localDeviceHeight), accountId, imageId);
                        drawPadEventListener.onDrawTouchDown(accountId, imageId);
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
    public void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId, String imageId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    if ((editTextFieldId != null && !editTextFieldId.isEmpty()) && (accountId != null && !accountId.isEmpty()) && (imageId != null && !imageId.isEmpty())) {
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

    public void onDrawCollabInvite(SignalingProto.DrawCollab drawCollabResponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    String fromAccountId = drawCollabResponse.getFromAccountId();
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
            }
        });
    }

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
    public void onUrlFetchSuccess(String janusBaseUrl, String apiKey, String apiSecret) {
        this.janusBaseUrl = janusBaseUrl;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        Log.d(TAG, "janus server info: " + janusBaseUrl + apiKey + apiSecret);
        if (videoCallListenerClient != null) {
            videoCallListenerClient.onJanusCredentialsReceived(janusBaseUrl, apiKey,
                    apiSecret, serviceName, serviceProfileUri, accountId);
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
        ClientActivity.launch(VideoCallHandleActivity.this,
                false, hostActivityCallbackClient, drawCallBack,
                serviceName, serviceProfileUri, accountType);//TODO: change it to SERVICE_PROVIDER_APP later
    }

    @Override
    public void onConnectionFail(String msg) {
        Banner.make(Objects.requireNonNull(this).getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
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
        videoCallInitiated = true;
        presenter.checkConnection(TreeleafMqttClient.mqttClient);
    }

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

}
