package com.treeleaf.anydone.serviceprovider.videocallreceive;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.protobuf.ByteString;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.januswebrtc.Callback;
import com.treeleaf.januswebrtc.RestChannel;
import com.treeleaf.januswebrtc.ServerActivity;
import com.treeleaf.januswebrtc.VideoCallUtil;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import java.math.BigInteger;

import static com.treeleaf.januswebrtc.Const.JOINEE_LOCAL;
import static com.treeleaf.januswebrtc.Const.JOINEE_REMOTE;

public class VideoReceiveActivity extends MvpBaseActivity
        <VideoCallReceivePresenterImpl> implements
        VideoCallReceiveContract.VideoCallReceiveActivityView, Callback.OnDrawEventListener {
    private static final String MQTT = "MQTT_EVENT_CHECK";
    private static final String TAG = "VideoReceiveActivity";
    Callback.HostActivityCallback hostActivityCallbackServer;

    private Account userAccount;
    private String accountId, accountName, accountPicture, rtcMessageId;
    private String serviceName, serviceProfileUri;
    private ServerActivity.VideoCallListener videoCallListenerServer;
    private ServerActivity.ServerDrawingPadEventListener serverDrawingPadEventListener;
    private boolean paymentSuccess = false;
    private RestChannel.Role mRole;
    private Callback.DrawCallBack drawCallBack;
    String callerName;
    String callerAccountId;
    String callerProfileUrl;
    private long serviceRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAccount = AccountRepo.getInstance().getAccount();
        accountId = userAccount.getAccountId();
        accountName = userAccount.getFullName();
        accountPicture = userAccount.getProfilePic();

        hostActivityCallbackServer = new Callback.HostActivityCallback() {

            @Override
            public void fetchJanusServerInfo() {

            }

            @Override
            public void specifyRole(RestChannel.Role role) {
                mRole = role;
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
                serverDrawingPadEventListener = (ServerActivity.ServerDrawingPadEventListener) drawPadEventListener;
            }

            @Override
            public void notifyHostHangUp() {
            }

            @Override
            public void notifySubscriberLeft() {
                presenter.publishParticipantLeftEvent(accountId, accountName, accountPicture, serviceRequestId);
            }

            @Override
            public void onPublisherVideoStarted() {
                presenter.publishSubscriberJoinEvent(accountId, accountName, accountPicture, serviceRequestId);
            }

            @Override
            public String getLocalAccountId() {
                return accountId;
            }

        };

        drawCallBack = new Callback.DrawCallBack() {

            @Override
            public void onNewImageAcknowledge(int width, int height, long timeStamp) {
                presenter.publishSendAckToRemoteEvent(accountId, accountName, accountPicture, serviceRequestId,
                        width, height, System.currentTimeMillis());
            }

            @Override
            public void onHoldDraw() {
                if (serverDrawingPadEventListener != null)
                    serverDrawingPadEventListener.onDrawShowProgress();//TODO: uncomment this later
            }

            @Override
            public void onDiscardDraw() {
                presenter.publishCancelDrawEvent(accountId, accountName, accountPicture, serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onDrawCanvasCleared() {
                presenter.publishDrawCanvasClearEvent(accountId, accountName, accountPicture, serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onReceiveNewTextField(float x, float y, String editTextFieldId) {
                presenter.publishDrawReceiveNewTextEvent(accountId, accountName, accountPicture, x, y,
                        editTextFieldId, serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onReceiveNewTextChange(String text, String id) {
                presenter.publishTextFieldChangeEventEvent(accountId, accountName, accountPicture, text,
                        id, serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onReceiveEdiTextRemove(String editTextId) {
                presenter.publishTextFieldRemoveEventEvent(accountId, accountName, accountPicture, editTextId,
                        serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onDrawParamChanged(CaptureDrawParam captureDrawParam) {
                presenter.publishDrawMetaChangeEvent(accountId, accountName, accountPicture, captureDrawParam.getXCoordinate(),
                        captureDrawParam.getYCoordinate(), captureDrawParam.getBrushWidth(),
                        Float.parseFloat(captureDrawParam.getBrushOpacity().toString()),
                        captureDrawParam.getBrushColor(), captureDrawParam.getTextColor(),
                        serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onStartDraw(float x, float y) {
                presenter.publishDrawTouchDownEvent(accountId, accountName, accountPicture,
                        serviceRequestId, x, y, System.currentTimeMillis());
            }

            @Override
            public void onClientTouchMove(CaptureDrawParam captureDrawParam) {
                presenter.publishDrawTouchMoveEvent(accountId, accountName, accountPicture,
                        serviceRequestId, captureDrawParam.getXCoordinate(), captureDrawParam.getYCoordinate(),
                        System.currentTimeMillis());
            }

            @Override
            public void onClientTouchUp() {
                presenter.publishDrawTouchUpEvent(accountId, accountName, accountPicture,
                        serviceRequestId, System.currentTimeMillis());
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
            int localDeviceWidth = VideoCallUtil.getDeviceResolution(VideoReceiveActivity.this)[0];
            int localDeviceHeight = VideoCallUtil.getDeviceResolution(VideoReceiveActivity.this)[1];
            presenter.publishSendImageToRemoteEvent(accountId, accountName, accountPicture, serviceRequestId, imageByteString,
                    localDeviceWidth, localDeviceHeight, System.currentTimeMillis());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected int getLayout() {
        return 0;
    }

    public void setServiceRequestId(long serviceId) {
        serviceRequestId = serviceId;
    }

    public void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                             boolean videoBroadcastPublish) {
        Log.d(MQTT, "onVideoRoomInitiationSuccess");
        rtcMessageId = broadcastVideoCall.getRtcMessageId();
        String janusServerUrl = broadcastVideoCall.getAvConnectDetails().getBaseUrl();
        String janusApiKey = broadcastVideoCall.getAvConnectDetails().getApiKey();
        String janusApiSecret = broadcastVideoCall.getAvConnectDetails().getApiSecret();
        String roomNumber = broadcastVideoCall.getRoomId();
        String participantId = broadcastVideoCall.getParticipantId();

        callerName = broadcastVideoCall.getSenderAccount().getFullName();
        callerAccountId = broadcastVideoCall.getSenderAccountId();
        callerProfileUrl = broadcastVideoCall.getSenderAccount().getProfilePic();

        ServerActivity.launch(this, janusServerUrl, janusApiKey, janusApiSecret,
                roomNumber, participantId, hostActivityCallbackServer, drawCallBack, callerName, callerProfileUrl);

    }

    public void onImageReceivedFromConsumer(int width, int height, long captureTime, byte[] convertedBytes, String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawNewImageCaptured(width, height, captureTime, convertedBytes, accountId);
                }
            }
        });

    }

    public void onImageDrawDiscardRemote(String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onImageReceivedFromConsumer");
                if (serverDrawingPadEventListener != null)
                    serverDrawingPadEventListener.onDrawDiscard(accountId);
            }
        });
    }

    public void onImageDrawDiscardLocal() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null)
                    serverDrawingPadEventListener.onDrawHideProgress();
            }
        });
    }

    public void onImageCaptured() {
        Log.d(TAG, "onImageReceivedFromConsumer");
        if (serverDrawingPadEventListener != null)
            serverDrawingPadEventListener.onDrawHideProgress();
    }

    public void onImageAckSent(String accountId) {
        if (serverDrawingPadEventListener != null)
            serverDrawingPadEventListener.onDrawDisplayCapturedImage(accountId);
    }

    public void onRemoteDeviceConfigReceived(SignalingProto.StartDrawAcknowledgement startDrawAckResponse, String accountId) {
        if (serverDrawingPadEventListener != null) {
            int width = startDrawAckResponse.getBitmapWidth();
            int height = startDrawAckResponse.getBitmapHeight();
            long timeStamp = startDrawAckResponse.getCapturedTime();
            serverDrawingPadEventListener.onDrawRemoteDeviceConfigReceived(width, height, timeStamp, accountId);
            serverDrawingPadEventListener.onDrawHideProgress();
        }
    }

    public void onVideoRoomJoinSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse) {
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

    public void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft) {
        Log.d(MQTT, "onParticipantLeft");
        if (videoCallListenerServer != null) {
            UserProto.Account account = participantLeft.getSenderAccount();
//            videoCallListenerServer.onJoineeRemoved(account.getAccountId());
            videoCallListenerServer.onJoineeRemoved(participantLeft.getSenderAccountId());
        }
    }

    public void onHostHangUp(SignalingProto.VideoRoomHostLeft videoRoomHostLeft) {
        Log.d(MQTT, "onHostHangUp");
        if (videoCallListenerServer != null)
            videoCallListenerServer.onHostTerminateCall();
    }

    @Override
    public void onDrawTouchDown(CaptureDrawParam captureDrawParam, String accountId) {
        if (serverDrawingPadEventListener != null) {
            serverDrawingPadEventListener.onDrawNewDrawCoordinatesReceived(captureDrawParam.getXCoordinate(),
                    captureDrawParam.getYCoordinate(), accountId);
            serverDrawingPadEventListener.onDrawTouchDown(accountId);
        }
    }

    @Override
    public void onDrawTouchMove(CaptureDrawParam captureDrawParam, String accountId) {
        if (serverDrawingPadEventListener != null) {
            serverDrawingPadEventListener.onDrawNewDrawCoordinatesReceived(captureDrawParam.getXCoordinate(),
                    captureDrawParam.getYCoordinate(), accountId);
            serverDrawingPadEventListener.onDrawTouchMove(accountId);
        }
    }

    @Override
    public void onDrawTouchUp(String accountId) {
        if (serverDrawingPadEventListener != null) {
            serverDrawingPadEventListener.onDrawTouchUp(accountId);
        }
    }

    @Override
    public void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawReceiveNewTextField(x, y, editTextFieldId, accountId);
                }
            }
        });
    }

    @Override
    public void onDrawReceiveNewTextChange(String text, String id, String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawReceiveNewTextChange(text, id, accountId);
                }
            }
        });
    }

    @Override
    public void onDrawReceiveEdiTextRemove(String editTextId, String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawReceiveEdiTextRemove(editTextId, accountId);
                }
            }
        });
    }

    @Override
    public void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId) {
        if (serverDrawingPadEventListener != null) {
            serverDrawingPadEventListener.onDrawParamChanged(captureDrawParam, accountId);
        }
    }

    @Override
    public void onDrawCanvasCleared(String accountId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawCanvasCleared(accountId);
                }
            }
        });

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

}
