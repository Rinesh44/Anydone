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
import static com.treeleaf.januswebrtc.Const.CONSUMER_APP;
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
            public void onDiscardDraw() {
                presenter.publishCancelDrawEvent(accountId, accountName, accountPicture, refId, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onDrawCanvasCleared() {
                presenter.publishDrawCanvasClearEvent(accountId, accountName, accountPicture, refId, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onReceiveNewTextField(float x, float y, String editTextFieldId) {
                presenter.publishDrawReceiveNewTextEvent(accountId, accountName, accountPicture, x, y,
                        editTextFieldId, refId, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onReceiveNewTextChange(String text, String id) {
                presenter.publishTextFieldChangeEventEvent(accountId, accountName, accountPicture, text,
                        id, refId, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onReceiveEdiTextRemove(String editTextId) {
                presenter.publishTextFieldRemoveEventEvent(accountId, accountName, accountPicture, editTextId,
                        refId, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onDrawParamChanged(CaptureDrawParam captureDrawParam) {
                presenter.publishDrawMetaChangeEvent(accountId, accountName, accountPicture, captureDrawParam.getXCoordinate(),
                        captureDrawParam.getYCoordinate(), captureDrawParam.getBrushWidth(),
                        Float.parseFloat(captureDrawParam.getBrushOpacity().toString()),
                        captureDrawParam.getBrushColor(), captureDrawParam.getTextColor(),
                        refId, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onStartDraw(float x, float y) {
                presenter.publishDrawTouchDownEvent(accountId, accountName, accountPicture,
                        refId, x, y, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onClientTouchMove(CaptureDrawParam captureDrawParam) {
                presenter.publishDrawTouchMoveEvent(accountId, accountName, accountPicture,
                        refId, captureDrawParam.getXCoordinate(), captureDrawParam.getYCoordinate(),
                        System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onClientTouchUp() {
                presenter.publishDrawTouchUpEvent(accountId, accountName, accountPicture,
                        refId, System.currentTimeMillis(), rtcContext);
            }

            @Override
            public void onNewImageFrameCaptured(Bitmap bitmap) {
                captureImageFrame(bitmap);
            }

        };
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

    public void onImageReceivedFromConsumer(int width, int height, long captureTime, byte[] convertedBytes, String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    remoteDeviceResolutions.put(accountId, new Integer[]{width, height});
                    drawPadEventListener.onDrawNewImageCaptured(width, height, captureTime, convertedBytes, accountId);
                }
            }
        });

    }

    public void onImageDrawDiscardRemote(String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onImageReceivedFromConsumer");
                if (drawPadEventListener != null)
                    drawPadEventListener.onDrawDiscard(accountId);
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

    public void onImageAckSent(String accountId) {
        if (drawPadEventListener != null)
            drawPadEventListener.onDrawDisplayCapturedImage(accountId);
    }

    public void onRemoteDeviceConfigReceived(SignalingProto.StartDrawAcknowledgement startDrawAckResponse, String accountId) {
        if (drawPadEventListener != null) {
            int width = startDrawAckResponse.getCanvasWidth();
            int height = startDrawAckResponse.getCanvasHeight();
            remoteDeviceResolutions.put(accountId, new Integer[]{width, height});
            long timeStamp = startDrawAckResponse.getCapturedTime();
            drawPadEventListener.onDrawRemoteDeviceConfigReceived(width, height, timeStamp, accountId);
            drawPadEventListener.onDrawHideProgress();
        }
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
    public void onDrawTouchDown(CaptureDrawParam captureDrawParam, String accountId) {
        if (drawPadEventListener != null) {
            drawPadEventListener.onDrawNewDrawCoordinatesReceived(adjustXPixelResolutions(captureDrawParam.getXCoordinate(), accountId),
                    adjustYPixelResolutions(captureDrawParam.getYCoordinate(), accountId), accountId);
            drawPadEventListener.onDrawTouchDown(accountId);
        }
    }

    @Override
    public void onDrawTouchMove(CaptureDrawParam captureDrawParam, String accountId) {
        if (drawPadEventListener != null) {
            drawPadEventListener.onDrawNewDrawCoordinatesReceived(adjustXPixelResolutions(captureDrawParam.getXCoordinate(),
                    accountId), adjustYPixelResolutions(captureDrawParam.getYCoordinate(), accountId), accountId);
            drawPadEventListener.onDrawTouchMove(accountId);
        }
    }

    @Override
    public void onDrawTouchUp(String accountId) {
        if (drawPadEventListener != null) {
            drawPadEventListener.onDrawTouchUp(accountId);
        }
    }

    @Override
    public void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    drawPadEventListener.onDrawReceiveNewTextField(adjustXPixelResolutions(x, accountId),
                            adjustYPixelResolutions(y, accountId), editTextFieldId, accountId);
                }
            }
        });
    }

    @Override
    public void onDrawReceiveNewTextChange(String text, String id, String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    drawPadEventListener.onDrawReceiveNewTextChange(text, id, accountId);
                }
            }
        });
    }

    @Override
    public void onDrawReceiveEdiTextRemove(String editTextId, String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    drawPadEventListener.onDrawReceiveEdiTextRemove(editTextId, accountId);
                }
            }
        });
    }

    @Override
    public void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId) {
        if (drawPadEventListener != null) {
            drawPadEventListener.onDrawParamChanged(captureDrawParam, accountId);
        }
    }

    @Override
    public void onDrawCanvasCleared(String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (drawPadEventListener != null) {
                    drawPadEventListener.onDrawCanvasCleared(accountId);
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
                serviceName, serviceProfileUri, CONSUMER_APP);//TODO: change it to SERVICE_PROVIDER_APP later
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

    public void checkConnection() {
        videoCallInitiated = true;
        presenter.checkConnection(TreeleafMqttClient.mqttClient);
    }

    private float adjustXPixelResolutions(float remoteX, String accountId) {
        if (remoteDeviceResolutions.get(accountId) == null) {
            return remoteX;
        }
        float adjustedWidth = VideoCallUtil.adjustXPixelResolutionInLocalDevice(localDeviceWidth,
                remoteDeviceResolutions.get(accountId)[0],
                remoteX) + VideoCallUtil.convertDpToPixel(0, VideoCallHandleActivity.this);
        return adjustedWidth;
    }

    private float adjustYPixelResolutions(float remoteY, String accountId) {
        if (remoteDeviceResolutions.get(accountId) == null) {
            return remoteY;
        }
        float adjustedHeight = VideoCallUtil.adjustYPixelResolutionInLocalDevice(localDeviceHeight,
                remoteDeviceResolutions.get(accountId)[1],
                remoteY) + VideoCallUtil.convertDpToPixel(0, VideoCallHandleActivity.this);
        return adjustedHeight;
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
