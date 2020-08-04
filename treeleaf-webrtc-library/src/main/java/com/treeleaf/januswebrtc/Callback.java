package com.treeleaf.januswebrtc;


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

        BigInteger getRoomNumber();

        void showProgressBar(String message);

        void hideProgressBar();

        void showVideoCallStartView(boolean visible);

        void updateProgressMessage(String message);

        void streamUnpublished(BigInteger roomId, BigInteger publisherId);

        void janusServerConfigurationSuccess(BigInteger sessionId, BigInteger roomId, BigInteger participantId);

        void startCreatingOffer(BigInteger handleId);

        void restError(String message);

        void onSlowLink();

    }


    interface IceConnectionChangeEvents {

        void iceConnectionChangeEvent(String event);

    }

    public interface HostActivityCallback {

        void passJanusServerInfo(BigInteger sessionId, BigInteger roomId, BigInteger participantId);

        void passJoineeReceivedCallback(ClientActivity.VideoCallListener videoCallListener);

        void notifyHostHangUp();

        void fetchJanusServerInfo();

    }

}
