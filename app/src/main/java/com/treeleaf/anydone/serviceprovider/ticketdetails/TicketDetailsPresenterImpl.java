package com.treeleaf.anydone.serviceprovider.ticketdetails;

import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;

import javax.inject.Inject;

public class TicketDetailsPresenterImpl extends BasePresenter<TicketDetailsContract.TicketDetailsView>
        implements TicketDetailsContract.TicketDetailsPresenter {
    public final String PUBLISH_TOPIC = "anydone/rtc/relay";
    private static final String TAG = "TicketDetailsPresenterI";
    private TicketDetailsRepository ticketDetailsRepository;


    @Inject
    public TicketDetailsPresenterImpl(TicketDetailsRepository ticketDetailsRepository) {
        this.ticketDetailsRepository = ticketDetailsRepository;
    }

    @Override
    public void publishSubscriberJoinEvent(String userAccountId, String accountName, String accountPicture,
                                           long ticketId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.VideoCallJoinRequest videoCallJoinRequest = SignalingProto.VideoCallJoinRequest.newBuilder()
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setRefId(String.valueOf(ticketId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.VIDEO_CALL_JOIN_REQUEST)
                .setVideoCallJoinRequest(videoCallJoinRequest)
                .setContext(AnydoneProto.ServiceContext.TICKET_CONTEXT)
                .build();


        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishParticipantLeftEvent(String userAccountId, String accountName, String accountPicture,
                                            long ticketId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.ParticipantLeft participantLeft = SignalingProto.ParticipantLeft.newBuilder()
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setRefId(String.valueOf(ticketId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.PARTICIPANT_LEFT_REQUEST)
                .setParticipantLeftRequest(participantLeft)
                .setContext(AnydoneProto.ServiceContext.TICKET_CONTEXT)
                .build();


        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

}
