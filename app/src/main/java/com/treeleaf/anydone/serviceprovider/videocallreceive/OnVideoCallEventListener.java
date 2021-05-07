package com.treeleaf.anydone.serviceprovider.videocallreceive;

import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

public interface OnVideoCallEventListener {

    void onMqttReponseArrived(String mqttReponseType, boolean isLocalResponse);

    void onVideoRoomInitiationSuccessClient(SignalingProto.BroadcastVideoCall broadcastVideoCall, AnydoneProto.ServiceContext context);

    void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall, boolean videoBroadcastPublish, AnydoneProto.ServiceContext context);

    void onLocalVideoRoomJoinSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse);

    void onRemoteVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse);

    void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft);

    void onHostHangUp(SignalingProto.VideoRoomHostLeft videoRoomHostLeft);

    void onCallDeclined(SignalingProto.ReceiverCallDeclined receiverCallDeclined);

    void onImageDrawDiscardRemote(String accountId, String imageId);

    void onMqttConnectionStatusChange(String connection);

    void onDrawTouchDown(CaptureDrawParam captureDrawParam, String accountId, String imageId, String fullName);

    void onDrawTouchMove(CaptureDrawParam captureDrawParam, String accountId, String imageId);

    void onDrawTouchUp(String accountId, String imageId);

    void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId, String imageId, CaptureDrawParam captureDrawParam);

    void onDrawReceiveNewTextChange(String text, String id, String accountId, String imageId);

    void onDrawReceiveEdiTextRemove(String editTextId, String accountId, String imageId);

    void onDrawPointerClicked(float x, float y, String accountId, String imageId, String fullName);

    void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId, String imageId);

    void onDrawCanvasCleared(String accountId, String imageId);

    void onDrawCollabInvite(SignalingProto.DrawCollab drawCollabResponse);

    void onDrawMaximize(SignalingProto.DrawMaximize drawMaximize);

    void onDrawMinimize(SignalingProto.DrawMinize drawMinize);

    void onDrawClose(SignalingProto.DrawClose drawClose);

    void onDrawMaxDrawingExceed(SignalingProto.MaxDrawingExceed drawClose);

}


