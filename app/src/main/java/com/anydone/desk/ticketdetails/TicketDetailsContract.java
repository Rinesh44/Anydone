package com.anydone.desk.ticketdetails;

import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.Conversation;

import org.eclipse.paho.client.mqttv3.MqttException;

public class TicketDetailsContract {

    public interface TicketDetailsView extends BaseView {

        void onLinkShareSuccess(String link);

        void onLinkShareFail(String msg);

        void onLocalVideoRoomJoinSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse);

        void onRemoteVideoRoomJoinedSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse);
        void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft);

        void onVideoRoomInvite(SignalingProto.AddCallParticipant broadcastVideoCall, AnydoneProto.ServiceContext context);

        void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                          boolean b, AnydoneProto.ServiceContext context);

        void onVideoRoomInitiationSuccessClient(SignalingProto.BroadcastVideoCall broadcastVideoCall, AnydoneProto.ServiceContext context);

        void onHostHangUp(SignalingProto.VideoRoomHostLeft videoRoomHostLeft);

        void onReceiverCallDeclined(SignalingProto.ReceiverCallDeclined receiverCallDeclined);

        void onMqttResponseReceivedChecked(String mqttResponseType, boolean localResponse);

        void onSubscribeFailMsg(Conversation conversation);

    }

    public interface TicketDetailsPresenter extends Presenter<TicketDetailsView> {

        void getShareLink(String ticketId);

        void setConversationAsFailed(Conversation conversation);

        void subscribeSuccessMessageAVCall(long orderId, String userAccountId) throws MqttException;

        void subscribeFailMessageAVCall(long refId) throws MqttException;

        void unSubscribeAVCall(String ticketId, String accountId) throws MqttException;

    }
}
