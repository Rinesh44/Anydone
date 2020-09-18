package com.treeleaf.januswebrtc;


import android.graphics.Bitmap;

import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.json.JSONObject;

import java.math.BigInteger;

public class Callback {

    //JanusRTCInterface
    interface JanusRTCInterface {
        void onPublisherJoined(BigInteger handleId);

        void onPublisherRemoteJsep(BigInteger handleId, JSONObject jsep);

        void onActivePublishserNotFound();

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

        void updateProgressMessage(String message);

        void streamUnpublished(BigInteger roomId, BigInteger publisherId);

        void janusServerConfigurationSuccess(BigInteger sessionId, BigInteger roomId, BigInteger participantId);

        void startCreatingOffer(BigInteger handleId);

        void restError(String message);

        void onSlowLink();

        void onHangUp();

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

        void specifyRole(RestChannel.Role role);

        void onPublisherVideoStarted();

        String getLocalAccountId();

    }

    public interface DrawCallBack {

        void onStartDraw(float x, float y);

        void onNewImageFrameCaptured(Bitmap bitmap);

        void onNewImageAcknowledge(int width, int height, long timeStamp);

        void onHoldDraw();

        void onDiscardDraw();

        void onDrawParamChanged(CaptureDrawParam captureDrawParam);

        void onClientTouchMove(CaptureDrawParam captureDrawParam);

        void onClientTouchUp();

        void onDrawCanvasCleared();

        void onReceiveNewTextField(float x, float y, String editTextFieldId);

        void onReceiveNewTextChange(String text, String id);

        void onReceiveEdiTextRemove(String editTextId);

    }

    public interface AudioVideoCallbackListener {

        void onJoineeReceived(String joineedName, String joineedProfileUrl, String accountId);

        void onJoineeRemoved(String accountId);

    }

    public interface DrawPadEventListener {
        /**
         * common events for draw between Client and Server
         */

        void onDrawNewImageCaptured(int width, int height, long captureTime, byte[] convertedBytes);

        void onDrawDisplayCapturedImage();

        void onDrawDiscard();

        void onDrawHideProgress();

        void onDrawShowProgress();

        void onDrawTouchDown(String accountId);

        void onDrawTouchMove();

        void onDrawTouchUp(String accountId);

        void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId);

        void onDrawReceiveNewTextChange(String text, String id, String accountId);

        void onDrawReceiveEdiTextRemove(String editTextId, String accountId);

        void onDrawParamChanged(CaptureDrawParam captureDrawParam);

        void onDrawNewDrawCoordinatesReceived(Float x, Float y);

        void onDrawCanvasCleared();

        void onDrawRemoteDeviceConfigReceived(int width, int height, long timeStamp);

    }

    public interface OnDrawEventListener {

        void onDrawTouchDown(CaptureDrawParam captureDrawParam, String accountId);

        void onDrawTouchMove(CaptureDrawParam captureDrawParam);

        void onDrawTouchUp(String accountId);

        void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId);

        void onDrawReceiveNewTextChange(String text, String id, String accountId);

        void onDrawReceiveEdiTextRemove(String editTextId, String accountId);

        void onDrawParamChanged(CaptureDrawParam captureDrawParam);

        void onDrawCanvasCleared();

    }

}
