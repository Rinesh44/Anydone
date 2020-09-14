package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;


import com.google.protobuf.ByteString;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;

import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;

import javax.inject.Inject;


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
                .setContext(AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT)
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
                .setContext(AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT)
                .build();


        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishSendImageToRemoteEvent(String userAccountId, String accountName, String accountPicture,
                                              long orderId, ByteString capturedImage, int bitmapWidth, int bitmapHeight,
                                              long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.StartDraw startDraw = SignalingProto.StartDraw.newBuilder()
                .setBitmapWidth(bitmapWidth)
                .setBitmapHeight(bitmapHeight)
                .setCapturedTime(capturedTime)
                .setCapturedImage(capturedImage)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.IMAGE_CAPTURE_MESSAGE_REQUEST)
                .setStartDrawRequest(startDraw)
                .build();


        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishSendAckToRemoteEvent(String userAccountId, String accountName, String accountPicture,
                                            long orderId, int bitmapWidth, int bitmapHeight, long capturedTime) {
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
    public void publishCancelDrawEvent(String userAccountId, String accountName, String accountPicture,
                                       long orderId, long cancellationTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.CancelDrawing cancelDrawing = SignalingProto.CancelDrawing.newBuilder()
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setCancellationTime(cancellationTime)
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.CANCEL_DRAWING_MESSAGE_REQUEST)
                .setCancelDrawRequest(cancelDrawing)
                .build();


        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawTouchDownEvent(String userAccountId, String accountName, String accountPicture,
                                          long orderId, Float x, Float y, long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawTouchDown drawTouchDown = SignalingProto.DrawTouchDown.newBuilder()
                .setX(x)
                .setY(y)
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_TOUCH_DOWN_REQUEST)
                .setDrawTouchDownRequest(drawTouchDown)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawTouchMoveEvent(String userAccountId, String accountName, String accountPicture,
                                          long orderId, Float x, Float y, long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawTouchMove drawTouchMove = SignalingProto.DrawTouchMove.newBuilder()
                .setX(x)
                .setY(y)
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_TOUCH_MOVE_REQUEST)
                .setDrawTouchMoveRequest(drawTouchMove)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawTouchUpEvent(String userAccountId, String accountName, String accountPicture,
                                        long orderId, long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawTouchUp drawTouchUp = SignalingProto.DrawTouchUp.newBuilder()
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_TOUCH_UP_REQUEST)
                .setDrawTouchUpRequest(drawTouchUp)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawMetaChangeEvent(String userAccountId, String accountName, String accountPicture,
                                           Float x, Float y, Float brushWidth, Float brushOpacity,
                                           int brushColor, int textColor, long orderId, long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawMetaDataChange drawMetaDataChange = SignalingProto.DrawMetaDataChange.newBuilder()
                .setX(x)
                .setY(y)
                .setBrushWidth(brushWidth)
                .setBrushOpacity(brushOpacity)
                .setBrushColor(brushColor)
                .setTextColor(textColor)
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_META_DATA_CHANGE_REQUEST)
                .setDrawMetaDataChangeRequest(drawMetaDataChange)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawCanvasClearEvent(String userAccountId, String accountName, String accountPicture,
                                            long orderId, long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawCanvasClear drawCanvasClear = SignalingProto.DrawCanvasClear.newBuilder()
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_CANVAS_CLEAR_REQUEST)
                .setDrawCanvasClearRequest(drawCanvasClear)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawReceiveNewTextEvent(String userAccountId, String accountName, String accountPicture,
                                               Float x, Float y, String textFieldId, long orderId, long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.ReceiveNewTextField receiveNewTextField = SignalingProto.ReceiveNewTextField.newBuilder()
                .setX(x)
                .setY(y)
                .setTextId(textFieldId)
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RECEIVE_NEW_TEXT_FIELD_REQUEST)
                .setReceiveNewTextFieldRequest(receiveNewTextField)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishTextFieldChangeEventEvent(String userAccountId, String accountName, String accountPicture,
                                                 String text, String textFieldId, long orderId, long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.TextFieldChange textFieldChange = SignalingProto.TextFieldChange.newBuilder()
                .setText(text)
                .setTextId(textFieldId)
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.TEXT_FIELD_CHANGE_REQUEST)
                .setTextFieldChangeRequest(textFieldChange)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishTextFieldRemoveEventEvent(String userAccountId, String accountName, String accountPicture,
                                                 String textFieldId, long orderId, long capturedTime) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.TextFieldRemove textFieldRemove = SignalingProto.TextFieldRemove.newBuilder()
                .setTextId(textFieldId)
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.TEXT_FIELD_REMOVE_REQUEST)
                .setTextFieldRemoveRequest(textFieldRemove)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

}