package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;


import android.text.TextUtils;

import com.google.protobuf.ByteString;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class ServiceRequestDetailActivityPresenterImpl extends
        BasePresenter<ServiceRequestDetailActivityContract.ServiceRequestDetailActivityView> implements
        ServiceRequestDetailActivityContract.ServiceRequestDetailActivityPresenter {

    private static final String TAG = "ServiceRequestDetailActivityPresenterImpl";
    public final String PUBLISH_TOPIC = "anydone/rtc/relay";
    private ServiceRequestDetailActivityRepository serviceRequestDetailActivityRepository;

    @Inject
    public ServiceRequestDetailActivityPresenterImpl(ServiceRequestDetailActivityRepository serviceRequestDetailActivityRepository) {
        this.serviceRequestDetailActivityRepository = serviceRequestDetailActivityRepository;
    }

    @Override
    public void fetchJanusServerUrl(String token) {
        serviceRequestDetailActivityRepository.fetchJanusServerUrl(token)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onNext(RtcServiceRpcProto.RtcServiceBaseResponse avConnectDetails) {
                        getView().hideProgressBar();
                        if (!(avConnectDetails.getAvConnectDetailsCount() > 0)) {
                            return;
                        }
                        List<SignalingProto.AvConnectDetails> avConnectDetailsList = avConnectDetails.getAvConnectDetailsList();

                        String janusBaseUrl = avConnectDetailsList.get(0).getBaseUrl();
                        String janusApiKey = avConnectDetailsList.get(0).getApiKey();
                        String janusApiSecret = avConnectDetailsList.get(0).getApiSecret();
                        if (avConnectDetails == null) {
                            getView().onUrlFetchFail("Fetching url failed.");
                            return;
                        }
                        if (TextUtils.isEmpty(janusBaseUrl) || TextUtils.isEmpty(janusApiKey) || TextUtils.isEmpty(janusApiSecret)) {
                            getView().onUrlFetchFail("Fetching url failed.");
                            return;
                        }

                        getView().onUrlFetchSuccess(janusBaseUrl, janusApiKey, janusApiSecret);

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onUrlFetchFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    public void publishVideoBroadCastMessage(String userAccountId, String accountName, String accountPicture, long orderId,
                                             String sessionId, String roomId, String participantId,
                                             String janusBaseUrl, String apiSecret, String apiKey) {
        String clientId = UUID.randomUUID().toString().replace("-", "");
        SignalingProto.AvConnectDetails avConnectDetails = SignalingProto.AvConnectDetails.newBuilder()
                .setBaseUrl(janusBaseUrl)
                .setApiKey(apiKey)
                .setApiSecret(apiSecret)
                .build();

        UserProto.Account account = UserProto.Account.newBuilder()
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.BroadcastVideoCall broadcastVideoCall = SignalingProto.BroadcastVideoCall.newBuilder()
                .setSessionId(sessionId)
                .setRoomId(roomId)
                .setParticipantId(participantId)
                .setAvConnectDetails(avConnectDetails)
                .setClientId(clientId)
                .setSenderAccountId(userAccountId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.VIDEO_CALL_BROADCAST)
                .setBroadcastVideoCall(broadcastVideoCall)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish response raw: " + message);
            }
        });
    }

    @Override
    public void publishHostHangUpEvent(String userAccountId, String accountName, String accountPicture,
                                       long orderId, String rtcMesssageId, boolean videoBroadCastPublish) {
        if (videoBroadCastPublish) {
            String clientId = UUID.randomUUID().toString().replace("-", "");

            UserProto.Account account = UserProto.Account.newBuilder()
                    .setFullName(accountName)
                    .setProfilePic(accountPicture)
                    .build();

            SignalingProto.VideoRoomHostLeft videoRoomHostLeft = SignalingProto.VideoRoomHostLeft.newBuilder()
                    .setSenderAccountId(userAccountId)
                    .setClientId(clientId)
                    .setRefId(String.valueOf(orderId))
                    .setSenderAccount(account)
                    .setRtcMessageId(rtcMesssageId == null ? "" : rtcMesssageId)
                    .build();

            RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                    .setRelayType(RtcProto.RelayRequest.RelayRequestType.VIDEO_ROOM_HOST_LEFT_REQUEST)
                    .setVideoRoomHostLeftRequest(videoRoomHostLeft)
                    .build();


            TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    GlobalUtils.showLog(TAG, "publish host left: " + message);
                }
            });
        }

    }

    /**
     * publish mqqt event notifying image is received from consumer
     * and is now ready to draw
     *
     * @param userAccountId
     * @param accountName
     * @param accountPicture
     * @param orderId
     * @param bitmapWidth
     * @param bitmapHeight
     * @param capturedTime
     */
    @Override
    public void publishImageCaptureReceivedEvent(String userAccountId, String accountName, String accountPicture,
                                                 long orderId, int bitmapWidth, int bitmapHeight,
                                                 long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.StartDrawAcknowledgement startDrawAcknowledgement = SignalingProto.StartDrawAcknowledgement.newBuilder()
                .setBitmapWidth(bitmapWidth)
                .setBitmapHeight(bitmapHeight)
                .setCapturedTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.CAPTURE_IMAGE_RECEIVED_RESPONSE_REQUEST)
                .setStartDrawAckRequest(startDrawAcknowledgement)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishSubscriberJoinEvent(String userAccountId, String accountName, String accountPicture,
                                           long orderId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.VideoCallJoinRequest videoCallJoinRequest = SignalingProto.VideoCallJoinRequest.newBuilder()
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.VIDEO_CALL_JOIN_REQUEST)
                .setVideoCallJoinRequest(videoCallJoinRequest)
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
                                            long orderId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.ParticipantLeft participantLeft = SignalingProto.ParticipantLeft.newBuilder()
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.PARTICIPANT_LEFT_REQUEST)
                .setParticipantLeftRequest(participantLeft)
                .build();


        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void checkConnection(MqttAndroidClient client) {
        if (!GlobalUtils.isConnected(getContext())) {
            getView().onConnectionFail("No Internet Connection");
        } else if (!client.isConnected()) {
            getView().onConnectionFail("Connection not established");
        } else {
            getView().onConnectionSuccess();
        }
    }

}