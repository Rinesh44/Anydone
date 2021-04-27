package com.treeleaf.januswebrtc;


import android.graphics.Bitmap;

import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.json.JSONObject;

import java.math.BigInteger;

public class Callback {

    //JanusRTCInterface
    interface JanusRTCInterface {

        void onPublisherRemoteJsep(BigInteger handleId, JSONObject jsep);

        void subscriberHandleRemoteJsep(BigInteger handleId, JSONObject jsep);

        void onLeaving(BigInteger handleId);
    }


    public interface ApiCallback {

        void onRoomCreated(BigInteger roomNumber);

        void onParticipantCreated(BigInteger participantId);

        void onRoomJoined(BigInteger roomNumber, String participantId);

        void onPublisherVideoStarted();

        BigInteger getRoomNumber();

        BigInteger getParticipantId();

        void showProgressBar(String message);

        void hideProgressBar();

        void showVideoCallStartView(boolean visible);

        void handleVideoCallViewForSingleCall();

        void stopAudioRinging();

        void updateProgressMessage(String message);

        void streamUnpublished(BigInteger roomId, BigInteger publisherId);

        void janusServerConfigurationSuccess(BigInteger sessionId, BigInteger roomId, BigInteger participantId);

        void startCreatingOffer(BigInteger handleId);

        void restError(String message);

        void onSlowLink(BigInteger bigInteger);

        void onHangUp();

        void onVideoRendered();

        void onParticipantLeftJanus(String participantId);

        void onActivePublisherNotFound();

    }


    interface ConnectionEvents {

        void iceConnectionChangeEvent(String event);

        void onRemoteVideoTrackAdded();

    }

    public interface HostActivityCallback {

        void passJanusServerInfo(BigInteger sessionId, BigInteger roomId, BigInteger participantId);

        void onServiceProviderAudioPublished(BigInteger sessionId, BigInteger roomId, BigInteger participantId);

        void passJoineeReceivedCallback(AudioVideoCallbackListener videoCallListener);

        void passDrawPadEventListenerCallback(DrawPadEventListener drawPadEventListener);

        void notifyHostHangUp();

        void notifySubscriberLeft();

        void fetchJanusServerInfo();

        void fetchCallerAndJanusCredentials();

        void specifyRole(RestChannel.Role role);

        void onPublisherVideoStarted();

        String getLocalAccountId();

        void unSubscribeVideoCallMqtt();

        void sendDrawingViewResolution(int localDeviceWidth, int localDeviceHeight);

        void closeAVCallNotification();

        Boolean isProductionEnvironment();

    }

    public interface DrawCallBack {

        void onStartDraw(float x, float y, CaptureDrawParam captureDrawParam, String imageId, String touchSessionId);

        void onDiscardDraw(String imageId);

        void onClientTouchMove(CaptureDrawParam captureDrawParam, String imageId, Float prevX, Float prevY, String touchSessionId);

        void onClientTouchUp(String imageId, String touchSessionId);

        void onDrawCanvasCleared(String imageId);

        void onReceiveNewTextField(float x, float y, String editTextFieldId, String imageId, CaptureDrawParam captureDrawParam);

        void onReceiveNewTextChange(String text, String id, String imageId);

        void onReceiveEdiTextRemove(String editTextId, String imageId);

        void onCollabInvite(Joinee joinee, String pictureId, Bitmap bitmap);

        void onMaximizeDrawing(String pictureId);

        void onMinimizeDrawing(String pictureId);

    }

    public interface AudioVideoCallbackListener {

        void onJoineeReceived(String joineeName, String joineedProfileUrl,
                              String accountId, String joineeType);

        void onJoineeRemoved(String accountId);

        void onMqttConnectionChanged(String status);

        void onParticipantLeft();

        void onMqttReponseArrived(String responseType, boolean isLocalResponse);

        void onCallDeclined();

    }

    public interface DrawPadEventListener {
        /**
         * common events for draw between Client and Server
         */

        void onDrawDisplayCapturedImage(String accountId);

        void onDrawDiscard(String accountId, String imageId);

        void onDrawTouchDown(String accountId, String imageId, String fullName);

        void onDrawTouchMove(String accountId, String imageId);

        void onDrawTouchUp(String accountId, String imageId);

        void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId, String imageId);

        void onDrawReceiveNewTextChange(String text, String id, String accountId, String imageId);

        void onDrawReceiveEdiTextRemove(String editTextId, String accountId, String imageId);

        void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId, String imageId);

        void onDrawNewDrawCoordinatesReceived(Float x, Float y, String accountId, String imageId);

        void onDrawCanvasCleared(String accountId, String imageId);

        void onDrawCollabInvite(String fromAccountId, String toAccountId, String imageId, byte[] convertedBytes);

        void onDrawMaximize(String fromAccountId, String imageId);

        void onDrawMinimize(String fromAccountId, String imageId);

        void onDrawClose(String fromAccountId, String imageId);

    }

}
