package com.treeleaf.januswebrtc;


import android.graphics.Bitmap;

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

        void passJoineeReceivedCallback(ClientActivity.VideoCallListener videoCallListener);

        void passJoineeReceivedCallback(ServerActivity.VideoCallListener videoCallListener);

        void notifyHostHangUp();

        void notifySubscriberLeft();

        void fetchJanusServerInfo();

        void specifyRole(RestChannel.Role role);

        void onPublisherVideoStarted();

        void passCapturedImageFrame(Bitmap bitmap);

        void showProgressBarUntilMqttResponse();

        void discardDraw();

    }

}
