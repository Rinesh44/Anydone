package com.anydone.desk.ticketdetails;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.mqtt.TreeleafMqttCallback;
import com.anydone.desk.mqtt.TreeleafMqttClient;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Conversation;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.ConversationRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.anydone.desk.utils.Constants.MQTT_LOG;
import static com.anydone.desk.utils.GlobalUtils.SHOW_MQTT_LOG;

public class TicketDetailsPresenterImpl extends BasePresenter<TicketDetailsContract.TicketDetailsView>
        implements TicketDetailsContract.TicketDetailsPresenter {
    public final String PUBLISH_TOPIC = "anydone/rtc/relay";
    private static final String TAG = "TicketDetailsPresenterI";
    private TicketDetailsRepository ticketDetailsRepository;
    private Account account = AccountRepo.getInstance().getAccount();

    @Inject
    public TicketDetailsPresenterImpl(TicketDetailsRepository ticketDetailsRepository) {
        this.ticketDetailsRepository = ticketDetailsRepository;
    }

    @Override
    public void getShareLink(String ticketId) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        TicketProto.GetSharableLinkRequest getSharableLinkRequest =
                TicketProto.GetSharableLinkRequest.newBuilder()
                        .setTicketId(ticketId)
                        .build();

        ticketBaseResponseObservable = service.getLink(token,
                getSharableLinkRequest);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       ticketBaseResponse) {
                                GlobalUtils.showLog(TAG, "get link response: "
                                        + ticketBaseResponse);

                                getView().hideProgressBar();
                                if (ticketBaseResponse == null) {
                                    getView().onLinkShareFail(
                                            "Link share failed");
                                    return;
                                }

                                if (ticketBaseResponse.getError()) {
                                    getView().onLinkShareFail(
                                            ticketBaseResponse.getMsg());
                                    return;
                                }

                                getView().onLinkShareSuccess(ticketBaseResponse.getLink());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onLinkShareFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void subscribeSuccessMessageAVCall(long ticketId, String userAccountId) throws MqttException {
        String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + userAccountId + "/avcall/" + ticketId;
        GlobalUtils.showLog(TAG, "subscribe topic: " + SUBSCRIBE_TOPIC);

        TreeleafMqttClient.subscribe(SUBSCRIBE_TOPIC, new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message)
                    throws InvalidProtocolBufferException {
                RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse
                        .parseFrom(message.getPayload());

                if (relayResponse.getRefId().equalsIgnoreCase(String.valueOf(ticketId))) {
                    if (true) {
                        //after click on kGraph

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .VIDEO_CALL_BROADCAST_RESPONSE)) {
                            SignalingProto.BroadcastVideoCall broadcastVideoCall =
                                    relayResponse.getBroadcastVideoCall();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + broadcastVideoCall.getSenderAccountId());
                            if (broadcastVideoCall != null) {
                                if (userAccountId.equals(broadcastVideoCall.getSenderAccountId())) {
                                    getView().onVideoRoomInitiationSuccessClient(broadcastVideoCall, relayResponse.getContext());
                                } else {
//                                    getView().onVideoRoomInitiationSuccess(broadcastVideoCall, true, relayResponse.getContext());
                                }
                                sendMqttLog("BROADCAST", userAccountId.equals(broadcastVideoCall.getSenderAccountId()));
                            }
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .ADD_CALL_PARTICIPANT)) {
                            SignalingProto.AddCallParticipant addCallParticipant =
                                    relayResponse.getAddCallParticipant();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + addCallParticipant.getSenderAccountId());
                            if (addCallParticipant != null) {
                                if (!userAccountId.equals(addCallParticipant.getSenderAccountId())) {
                                    if (addCallParticipant.getAccountIdsList().contains(userAccountId)) {
//                                        getView().onVideoRoomInvite(addCallParticipant, relayResponse.getContext());
                                    }
                                }
                                sendMqttLog("CALL JOIN INVITE", userAccountId.equals(addCallParticipant.getSenderAccountId()));
                            }
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .PARTICIPANT_LEFT_RESPONSE)) {
                            SignalingProto.ParticipantLeft participantLeft =
                                    relayResponse.getParticipantLeftResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + participantLeft.getSenderAccount().getAccountId());
                            if (participantLeft != null) {
                                getView().onParticipantLeft(participantLeft);
//                                if (userAccountId.equals(participantLeft.getSenderAccount().getAccountId()))
                            }
                            sendMqttLog("PARTICIPANT_LEFT", participantLeft.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .VIDEO_CALL_JOIN_RESPONSE)) {
                            SignalingProto.VideoCallJoinResponse videoCallJoinResponse =
                                    relayResponse.getVideoCallJoinResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + videoCallJoinResponse.getSenderAccount().getAccountId());
                            if (videoCallJoinResponse != null) {
                                if (!userAccountId.equals(videoCallJoinResponse.getSenderAccountId())) {
                                    getView().onRemoteVideoRoomJoinedSuccess(videoCallJoinResponse);
                                } else {
                                    getView().onLocalVideoRoomJoinSuccess(videoCallJoinResponse);
                                }
                                sendMqttLog("JOIN", videoCallJoinResponse.getSenderAccount().getAccountId().
                                        equals(userAccountId));
                            }
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .VIDEO_ROOM_HOST_LEFT_RESPONSE)) {
                            SignalingProto.VideoRoomHostLeft videoRoomHostLeft = relayResponse
                                    .getVideoRoomHostLeftResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + videoRoomHostLeft.getSenderAccount().getAccountId());
                            if (videoRoomHostLeft != null && !userAccountId.equals(videoRoomHostLeft.getSenderAccount().getAccountId())) {
                                getView().onHostHangUp(videoRoomHostLeft);
                            }
                            sendMqttLog("HOST_LEFT", videoRoomHostLeft.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .RECEIVER_CALL_DECLINED_RESPONSE)) {
                            GlobalUtils.showLog(TAG, "host left");
                            SignalingProto.ReceiverCallDeclined receiverCallDeclined = relayResponse
                                    .getReceiverCallDeclinedResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + receiverCallDeclined.getSenderAccount().getAccountId());
                            GlobalUtils.showLog(TAG, "user id check: " + userAccountId);
                            getView().onReceiverCallDeclined(receiverCallDeclined);
                            sendMqttLog("CALL_DECLINED", receiverCallDeclined.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                    }
                }
            }

        });
    }

    @Override
    public void subscribeFailMessageAVCall(long refId) throws MqttException {
        String ERROR_TOPIC = "anydone/rtc/relay/response/error/" + account.getAccountId() + "/avcall/" + refId;

        GlobalUtils.showLog(TAG, "error topic: " + ERROR_TOPIC);

        TreeleafMqttClient.subscribe(ERROR_TOPIC, new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message)
                    throws InvalidProtocolBufferException {
                GlobalUtils.showLog(TAG, "subscribe error response: " + message);

                RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse.parseFrom
                        (message.getPayload());
                GlobalUtils.showLog(TAG, "Msg publish fail: " + relayResponse);
                String clientId = relayResponse.getRelayError().getClientId();

                new Handler(Looper.getMainLooper()).post(() -> {
                    Conversation conversation = ConversationRepo.getInstance()
                            .getConversationByClientId(clientId);
                    setConversationAsFailed(conversation);
                });
            }
        });
    }


    @Override
    public void unSubscribeAVCall(String ticketId, String accountId) throws MqttException {//TODO: ask rinesh how to unsubscribe
        Log.d(MQTT_LOG, "unsubscribe av call mqtt");
        String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + accountId + "/avcall/" + ticketId;
        String ERROR_TOPIC = "anydone/rtc/relay/response/error/" + accountId + "/avcall/" + ticketId;//TODO: ask rinesh/kshitij error topic for video call
        TreeleafMqttClient.unsubscribe(SUBSCRIBE_TOPIC);
        TreeleafMqttClient.unsubscribe(ERROR_TOPIC);
    }

    @Override
    public void setConversationAsFailed(Conversation conversation) {
        if (conversation != null) {
            ConversationRepo.getInstance().onConversationSendFailed(conversation,
                    new Repo.Callback() {
                        @Override
                        public void success(Object o) {
                            GlobalUtils.showLog(TAG, "handled failed case");
                            getView().onSubscribeFailMsg(conversation);
                        }

                        @Override
                        public void fail() {
                            GlobalUtils.showLog(TAG,
                                    "failed to handle case");
                        }
                    });
        }
    }


    public void sendMqttLog(String eventName, boolean ownResponse) {
        if (SHOW_MQTT_LOG)
            getView().onMqttResponseReceivedChecked(eventName, ownResponse);
    }

}
