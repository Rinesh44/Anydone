package com.treeleaf.anydone.serviceprovider.videocallreceive;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivityRepository;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.CANCEL_DRAWING_MESSAGE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_CLOSE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_COLLAB_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_MAXIMIZE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_MINIMIZE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_START_RESPONSE;
import static com.treeleaf.anydone.serviceprovider.utils.Constants.MQTT_LOG;
import static com.treeleaf.anydone.serviceprovider.utils.Constants.RTC_CONTEXT_SERVICE_REQUEST;

public class VideoCallReceivePresenterImpl extends
        BasePresenter<VideoCallReceiveContract.VideoCallReceiveActivityView> implements
        VideoCallReceiveContract.VideoCallReceiveActivityPresenter {
    private static final String TAG = "ServiceRequestDetailActivityPresenterImpl";
    public final String PUBLISH_TOPIC = "anydone/rtc/relay";
    private ServiceRequestDetailActivityRepository serviceRequestDetailActivityRepository;

    @Inject
    public VideoCallReceivePresenterImpl(ServiceRequestDetailActivityRepository serviceRequestDetailActivityRepository) {
        this.serviceRequestDetailActivityRepository = serviceRequestDetailActivityRepository;
    }

    @Override
    public void subscribeSuccessMessageDrawing(String ticketId, String userAccountId) throws MqttException {

        String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + userAccountId + "/drawing/" + ticketId;
        GlobalUtils.showLog(TAG, "subscribe topic: " + SUBSCRIBE_TOPIC);

        TreeleafMqttClient.subscribe(SUBSCRIBE_TOPIC, new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message)
                    throws InvalidProtocolBufferException {
                RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse
                        .parseFrom(message.getPayload());

                if (relayResponse.getRefId().equalsIgnoreCase(ticketId)) {
                    if (true) {
                        //after click on kGraph

                        if (relayResponse.getResponseType().equals(CANCEL_DRAWING_MESSAGE_RESPONSE)) {
                            SignalingProto.CancelDrawing cancelDrawing = relayResponse.getCancelDrawResponse();
                            if (cancelDrawing != null) {
                                GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + cancelDrawing.getSenderAccount().getAccountId());
                                if (cancelDrawing.getSenderAccount().getAccountId().
                                        equals(userAccountId)) {
//                                    getView().onImageDrawDiscardLocal();
                                } else
                                    getView().onImageDrawDiscardRemote(cancelDrawing.getSenderAccount().getAccountId(),
                                            cancelDrawing.getImageId());
                                sendMqttLog("CANCEL DRAW", cancelDrawing.getSenderAccount().getAccountId().
                                        equals(userAccountId));
                            }
                        }

                        if (relayResponse.getResponseType().equals(DRAW_START_RESPONSE)) {
                            SignalingProto.DrawStart drawStartResponse = relayResponse
                                    .getDrawStartResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " +
                                    drawStartResponse.getSenderAccount().getAccountId() + " " + drawStartResponse.getImageId() + " " +
                                    drawStartResponse.getX() + ", " +
                                    drawStartResponse.getY() + ", brushwidth: " + drawStartResponse.getDrawMetaData().getBrushWidth()
                                    + ", brushopacity " + drawStartResponse.getDrawMetaData().getBrushOpacity()
                                    + ", brushcolor: " + drawStartResponse.getDrawMetaData().getBrushColor()
                                    + ", textcolor: " + drawStartResponse.getDrawMetaData().getTextColor()
                            );
                            if (drawStartResponse != null &&
                                    !drawStartResponse.getSenderAccount().getAccountId().equals(userAccountId)) {
                                CaptureDrawParam captureDrawParam = new CaptureDrawParam();
                                captureDrawParam.setXCoordinate(drawStartResponse.getX());
                                captureDrawParam.setYCoordinate(drawStartResponse.getY());
                                float brushWidth = drawStartResponse.getDrawMetaData().getBrushWidth();
                                captureDrawParam.setBrushWidth(brushWidth > 100.0 ? 100f : brushWidth);
                                float opacity = drawStartResponse.getDrawMetaData().getBrushOpacity();
                                captureDrawParam.setBrushOpacity(opacity > 1.0 ? ((int) (1.0 * 255)) : ((int) (opacity * 255)));
                                captureDrawParam.setBrushColor(Color.parseColor(drawStartResponse.getDrawMetaData().getBrushColor()));
                                captureDrawParam.setTextColor(Color.parseColor(drawStartResponse.getDrawMetaData().getTextColor()));
                                getView().onDrawTouchDown(captureDrawParam, drawStartResponse.getSenderAccount().getAccountId(),
                                        drawStartResponse.getImageId());
                            }
                            sendMqttLog("DRAW START " + drawStartResponse.getX() + " " +
                                    drawStartResponse.getY() + "timestamp: " + drawStartResponse.getEventTime(), drawStartResponse.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .DRAW_TOUCH_MOVE_RESPONSE)) {
                            SignalingProto.DrawTouchMove drawTouchMove = relayResponse
                                    .getDrawTouchMoveResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " +
                                    drawTouchMove.getSenderAccount().getAccountId() + " " + drawTouchMove.getImageId() + " " +
                                    drawTouchMove.getX() + ", " + drawTouchMove.getY());
                            if (drawTouchMove != null &&
                                    !drawTouchMove.getSenderAccount().getAccountId().equals(userAccountId)) {
                                CaptureDrawParam captureDrawParam = new CaptureDrawParam();
                                captureDrawParam.setXCoordinate(drawTouchMove.getX());
                                captureDrawParam.setYCoordinate(drawTouchMove.getY());
                                getView().onDrawTouchMove(captureDrawParam, drawTouchMove.getSenderAccount().getAccountId(),
                                        drawTouchMove.getImageId());
                            }
                            sendMqttLog("DRAW MOVE " + drawTouchMove.getX() + " " +
                                    drawTouchMove.getY() + "timestamp: " + drawTouchMove.getEventTime(), drawTouchMove.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .DRAW_END_RESPONSE)) {
                            SignalingProto.DrawEnd drawEndResponse = relayResponse
                                    .getDrawEndResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + drawEndResponse.getSenderAccount().getAccountId());
                            if (drawEndResponse != null &&
                                    !drawEndResponse.getSenderAccount().getAccountId().equals(userAccountId)) {
                                getView().onDrawTouchUp(drawEndResponse.getSenderAccount().getAccountId(), drawEndResponse.getImageId());
                            }
                            sendMqttLog("DRAW END" + " timestamp: " + drawEndResponse.getEventTime(), drawEndResponse.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .RECEIVE_NEW_TEXT_FIELD_RESPONSE)) {
                            SignalingProto.ReceiveNewTextField receiveNewTextField = relayResponse
                                    .getReceiveNewTextFieldResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + receiveNewTextField.getSenderAccount().getAccountId());
                            if (receiveNewTextField != null &&
                                    !receiveNewTextField.getSenderAccount().getAccountId().equals(userAccountId)) {

                                CaptureDrawParam captureDrawParam = new CaptureDrawParam();
                                captureDrawParam.setXCoordinate(receiveNewTextField.getX());
                                captureDrawParam.setYCoordinate(receiveNewTextField.getY());
                                float brushWidth = receiveNewTextField.getDrawMetaData().getBrushWidth();
                                captureDrawParam.setBrushWidth(brushWidth > 100.0 ? 100f : brushWidth);
                                float opacity = receiveNewTextField.getDrawMetaData().getBrushOpacity();
                                captureDrawParam.setBrushOpacity(opacity > 1.0 ? ((int) (1.0 * 255)) : ((int) (opacity * 255)));
                                captureDrawParam.setBrushColor(Color.parseColor(receiveNewTextField.getDrawMetaData().getBrushColor()));
                                captureDrawParam.setTextColor(Color.parseColor(receiveNewTextField.getDrawMetaData().getTextColor()));

                                getView().onDrawReceiveNewTextField(receiveNewTextField.getX(),
                                        receiveNewTextField.getY(), receiveNewTextField.getTextId(),
                                        receiveNewTextField.getSenderAccount().getAccountId(),
                                        receiveNewTextField.getImageId(), captureDrawParam);
                            }
                            sendMqttLog("NEW TEXT", receiveNewTextField.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .TEXT_FIELD_CHANGE_RESPONSE)) {
                            SignalingProto.TextFieldChange textFieldChange = relayResponse
                                    .getTextFieldChangeResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + textFieldChange.getSenderAccount().getAccountId());
                            if (textFieldChange != null &&
                                    !textFieldChange.getSenderAccount().getAccountId().equals(userAccountId)) {
                                getView().onDrawReceiveNewTextChange(textFieldChange.getText(),
                                        textFieldChange.getTextId(), textFieldChange.getSenderAccount().getAccountId(),
                                        textFieldChange.getImageId());
                            }
                            sendMqttLog("TEXT CHANGE", textFieldChange.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .TEXT_FIELD_REMOVE_RESPONSE)) {
                            SignalingProto.TextFieldRemove textFieldRemove = relayResponse
                                    .getTextFieldRemoveResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + textFieldRemove.getSenderAccount().getAccountId());
                            if (textFieldRemove != null &&
                                    !textFieldRemove.getSenderAccount().getAccountId().equals(userAccountId)) {
                                getView().onDrawReceiveEdiTextRemove(textFieldRemove.getTextId(),
                                        textFieldRemove.getSenderAccount().getAccountId(), textFieldRemove.getImageId());
                            }
                            sendMqttLog("TEXT REMOVE", textFieldRemove.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .DRAW_META_DATA_CHANGE_RESPONSE)) {
                            SignalingProto.DrawMetaDataChange drawMetaDataChange = relayResponse
                                    .getDrawMetaDataChangeResponse();
                            if (drawMetaDataChange != null &&
                                    !drawMetaDataChange.getSenderAccount().getAccountId().equals(userAccountId)) {
                                CaptureDrawParam captureDrawParam = new CaptureDrawParam();
                                captureDrawParam.setXCoordinate(drawMetaDataChange.getX());
                                captureDrawParam.setYCoordinate(drawMetaDataChange.getY());
                                captureDrawParam.setBrushWidth(drawMetaDataChange.getBrushWidth());
                                captureDrawParam.setBrushOpacity((int) drawMetaDataChange.getBrushOpacity());
                                captureDrawParam.setBrushColor(drawMetaDataChange.getBrushColor());
                                captureDrawParam.setTextColor(drawMetaDataChange.getTextColor());
                                getView().onDrawParamChanged(captureDrawParam, drawMetaDataChange.getSenderAccount().getAccountId(),
                                        drawMetaDataChange.getImageId());
                            }
                            sendMqttLog("META CHANGE", drawMetaDataChange.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .DRAW_CANVAS_CLEAR_RESPONSE)) {
                            SignalingProto.DrawCanvasClear drawCanvasClear = relayResponse
                                    .getDrawCanvasClearResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + drawCanvasClear.getSenderAccount().getAccountId());
                            if (drawCanvasClear != null &&
                                    !drawCanvasClear.getSenderAccount().getAccountId().equals(userAccountId)) {
                                getView().onDrawCanvasCleared(drawCanvasClear.getSenderAccount().getAccountId(),
                                        drawCanvasClear.getImageId());
                            }
                            sendMqttLog("CANVAS CLEAR", drawCanvasClear.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                        if (relayResponse.getResponseType().equals(DRAW_COLLAB_RESPONSE)) {
                            SignalingProto.DrawCollab drawCollabResponse = relayResponse.getDrawCollabResponse();
                            String accountId = drawCollabResponse.getSenderAccount().getAccountId();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + accountId);
                            if (drawCollabResponse != null) {
                                if (userAccountId.equals(accountId)) {
                                    //sent and received id is same
                                } else {
                                    //sent and received id is different
                                    if (drawCollabResponse.getToAccountId().equals(userAccountId)) {
                                        //handle collab response only if it is sent to me
                                        getView().onDrawCollabInvite(drawCollabResponse);
                                    }

                                }
                                sendMqttLog("COLLAB", drawCollabResponse.getSenderAccount().getAccountId().
                                        equals(userAccountId));
                            }
                        }

                        if (relayResponse.getResponseType().equals(DRAW_MAXIMIZE_RESPONSE)) {
                            SignalingProto.DrawMaximize drawMaximizeResponse = relayResponse.getDrawMaximizeResponse();
                            String accountId = drawMaximizeResponse.getSenderAccount().getAccountId();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + accountId + " on image " + drawMaximizeResponse.getImageId());
                            if (drawMaximizeResponse != null) {
                                if (userAccountId.equals(accountId)) {
                                    //sent and received id is same

                                } else {
                                    //sent and received id is different
                                    getView().onDrawMaximize(drawMaximizeResponse);
                                }
                                sendMqttLog("MAXIMIZE", drawMaximizeResponse.getSenderAccount().getAccountId().
                                        equals(userAccountId));
                            }
                        }

                        if (relayResponse.getResponseType().equals(DRAW_MINIMIZE_RESPONSE)) {
                            SignalingProto.DrawMinize drawMinimizeResponse = relayResponse.getDrawMinimizeResponse();
                            String accountId = drawMinimizeResponse.getSenderAccount().getAccountId();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + accountId + " on image " + drawMinimizeResponse.getImageId());
                            if (drawMinimizeResponse != null) {
                                if (userAccountId.equals(accountId)) {
                                    //sent and received id is same

                                } else {
                                    //sent and received id is different
                                    getView().onDrawMinimize(drawMinimizeResponse);
                                }
                                sendMqttLog("MINIMIZE", drawMinimizeResponse.getSenderAccount().getAccountId().
                                        equals(userAccountId));
                            }
                        }

                        if (relayResponse.getResponseType().equals(DRAW_CLOSE_RESPONSE)) {
                            SignalingProto.DrawClose drawClose = relayResponse.getDrawCloseResponse();
                            String accountId = drawClose.getSenderAccount().getAccountId();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + accountId);
                            if (drawClose != null) {
                                if (userAccountId.equals(accountId)) {
                                    //sent and received id is same

                                } else {
                                    //sent and received id is different
                                    getView().onDrawClose(drawClose);
                                }
                                sendMqttLog("DRAW CLOSE", drawClose.getSenderAccount().getAccountId().
                                        equals(userAccountId));
                            }
                        }


                    }
                }
            }

        });
    }

    @Override
    public void subscribeFailMessageDrawing(String ticketId, String accountId) throws MqttException {
        getView().hideProgressBar();
        String ERROR_TOPIC = "anydone/rtc/relay/response/error/" + accountId + "/drawing/" + ticketId;//TODO: ask rinesh/kshitij what is error topic

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

                });
            }
        });
    }

    @Override
    public void unSubscribeDrawing(String ticketId, String accountId) throws MqttException {//TODO: ask rinesh how to unsubscribe
        String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + accountId + "/drawing/" + ticketId;
        String ERROR_TOPIC = "anydone/rtc/relay/response/error/" + accountId + "/drawing/" + ticketId;//TODO: ask rinesh/kshitij error topic for video call
        TreeleafMqttClient.unsubscribe(SUBSCRIBE_TOPIC);
        TreeleafMqttClient.unsubscribe(ERROR_TOPIC);
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
                                             String janusBaseUrl, String apiSecret, String apiKey, String rtcContext) {
        String clientId = UUID.randomUUID().toString().replace("-", "");
        SignalingProto.AvConnectDetails avConnectDetails = SignalingProto.AvConnectDetails.newBuilder()
                .setBaseUrl(janusBaseUrl)
                .setApiKey(apiKey)
                .setApiSecret(apiSecret)
                .build();

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
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
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                       long orderId, String rtcMesssageId, boolean videoBroadCastPublish,
                                       String rtcContext) {
        if (videoBroadCastPublish) {
            String clientId = UUID.randomUUID().toString().replace("-", "");

            UserProto.Account account = UserProto.Account.newBuilder()
                    .setAccountId(userAccountId)
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
                    .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
                    .build();


            TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    GlobalUtils.showLog(TAG, "publish host left: " + message);
                }
            });
        }

    }

    @Override
    public void publishSubscriberJoinEvent(String userAccountId, String accountName, String accountPicture,
                                           long orderId, String rtcContext) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
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
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                            long orderId, String rtcContext) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
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
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                              long capturedTime, String rtcContext) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.StartDraw startDraw = SignalingProto.StartDraw.newBuilder()
                .setCanvasWidth(bitmapWidth)
                .setCanvasHeight(bitmapHeight)
                .setCapturedTime(capturedTime)
                .setCapturedImage(capturedImage)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.IMAGE_CAPTURE_MESSAGE_REQUEST)
                .setStartDrawRequest(startDraw)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                            long orderId, int bitmapWidth, int bitmapHeight, long capturedTime, String rtcContext) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.StartDrawAcknowledgement startDrawAcknowledgement = SignalingProto.StartDrawAcknowledgement.newBuilder()
                .setCanvasWidth(bitmapWidth)
                .setCanvasHeight(bitmapHeight)
                .setCapturedTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.CAPTURE_IMAGE_RECEIVED_RESPONSE_REQUEST)
                .setStartDrawAckRequest(startDrawAcknowledgement)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                       long orderId, long cancellationTime, String rtcContext, String imageId) {
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
                .setImageId(imageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.CANCEL_DRAWING_MESSAGE_REQUEST)
                .setCancelDrawRequest(cancelDrawing)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                          long orderId, Float x, Float y, CaptureDrawParam captureDrawParam,
                                          long capturedTime, String rtcContext, String imageId, String touchSessionId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawMetaData drawMetaData = SignalingProto.DrawMetaData.newBuilder()
                .setX(x)
                .setY(y)
                .setBrushWidth(captureDrawParam.getBrushWidth())
                .setBrushOpacity((float) captureDrawParam.getBrushOpacity() / (float) 255)
                .setBrushColor(String.format("#%06X", (0xFFFFFF & captureDrawParam.getBrushColor())))
                .setTextColor(String.format("#%06X", (0xFFFFFF & captureDrawParam.getTextColor())))
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(imageId)
                .build();

        SignalingProto.DrawStart drawStart = SignalingProto.DrawStart.newBuilder()
                .setX(x)
                .setY(y)
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(imageId)
                .setDrawSessionId(touchSessionId)
                .setDrawMetaData(drawMetaData)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_START_REQUEST)
                .setDrawStartRequest(drawStart)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                          long orderId, CaptureDrawParam captureDrawParam, Float prevX, Float prevY,
                                          long capturedTime, String rtcContext, String imageId, String touchSessionId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawMetaData drawMetaData = SignalingProto.DrawMetaData.newBuilder()
                .setX(captureDrawParam.getXCoordinate())
                .setY(captureDrawParam.getYCoordinate())
                .setBrushWidth(captureDrawParam.getBrushWidth())
                .setBrushOpacity((float) captureDrawParam.getBrushOpacity() / (float) 255)
                .setBrushColor(String.format("#%06X", (0xFFFFFF & captureDrawParam.getBrushColor())))
                .setTextColor(String.format("#%06X", (0xFFFFFF & captureDrawParam.getTextColor())))
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(imageId)
                .build();

        SignalingProto.DrawTouchMove drawTouchMove = SignalingProto.DrawTouchMove.newBuilder()
                .setX(captureDrawParam.getXCoordinate())
                .setY(captureDrawParam.getYCoordinate())
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(imageId)
                .setDrawSessionId(touchSessionId)
                .setDrawMetaData(drawMetaData)
                .setPrevX(prevX)
                .setPrevY(prevY)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_TOUCH_MOVE_REQUEST)
                .setDrawTouchMoveRequest(drawTouchMove)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                        long orderId, long capturedTime, String rtcContext, String imageId, String touchSessionId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawEnd drawEnd = SignalingProto.DrawEnd.newBuilder()
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(imageId)
                .setDrawSessionId(touchSessionId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_END_REQUEST)
//                .setDrawTouchUpRequest(drawTouchUp)
                .setDrawEndRequest(drawEnd)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                           int brushColor, int textColor, long orderId, long capturedTime, String rtcContext, String imageId) {
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
                .setImageId(imageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_META_DATA_CHANGE_REQUEST)
                .setDrawMetaDataChangeRequest(drawMetaDataChange)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                            long orderId, long capturedTime, String rtcContext, String imageId) {
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
                .setImageId(imageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_CANVAS_CLEAR_REQUEST)
                .setDrawCanvasClearRequest(drawCanvasClear)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                               Float x, Float y, String textFieldId, long orderId, long capturedTime, String rtcContext, String imageId, CaptureDrawParam captureDrawParam) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawMetaData drawMetaData = SignalingProto.DrawMetaData.newBuilder()
                .setX(captureDrawParam.getXCoordinate())
                .setY(captureDrawParam.getYCoordinate())
                .setBrushWidth(captureDrawParam.getBrushWidth())
                .setBrushOpacity((float) captureDrawParam.getBrushOpacity() / (float) 255)
                .setBrushColor(String.format("#%06X", (0xFFFFFF & captureDrawParam.getBrushColor())))
                .setTextColor(String.format("#%06X", (0xFFFFFF & captureDrawParam.getTextColor())))
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(imageId)
                .build();

        SignalingProto.ReceiveNewTextField receiveNewTextField = SignalingProto.ReceiveNewTextField.newBuilder()
                .setX(x)
                .setY(y)
                .setTextId(textFieldId)
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(imageId)
                .setDrawMetaData(drawMetaData)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RECEIVE_NEW_TEXT_FIELD_REQUEST)
                .setReceiveNewTextFieldRequest(receiveNewTextField)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                                 String text, String textFieldId, long orderId, long capturedTime, String rtcContext, String imageId) {
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
                .setImageId(imageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.TEXT_FIELD_CHANGE_REQUEST)
                .setTextFieldChangeRequest(textFieldChange)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
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
                                                 String textFieldId, long orderId, long capturedTime, String rtcContext, String imageId) {
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
                .setImageId(imageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.TEXT_FIELD_REMOVE_REQUEST)
                .setTextFieldRemoveRequest(textFieldRemove)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishInviteToCollabRequest(String fromAccountId, String toAccountId, String pictureId,
                                             String accountName, String accountPicture, long orderId,
                                             ByteString capturedImage, long capturedTime, String rtcContext) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(fromAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawCollab drawCollab = SignalingProto.DrawCollab.newBuilder()
                .setEventTime(capturedTime)
                .setSenderAccountId(fromAccountId)
                .setCapturedImage(capturedImage)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setFromAccountId(fromAccountId)
                .setToAccountId(toAccountId)
                .setSenderAccount(account)
                .setImageId(pictureId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_COLLAB_REQUEST)
                .setDrawCollabReq(drawCollab)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ? AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT : AnydoneProto.ServiceContext.TICKET_CONTEXT)
                .build();

        GlobalUtils.showLog(MQTT_LOG, "publish invite to collab");
        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawMaximize(String userAccountId, String pictureId, String accountName, String accountPicture,
                                    long orderId, long eventTime, String rtcContext) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawMaximize drawMaximize = SignalingProto.DrawMaximize.newBuilder()
                .setEventTime(eventTime)
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(pictureId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_MAXIMIZE_REQUEST)
                .setDrawMaximizeReq(drawMaximize)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ?
                        AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT :
                        AnydoneProto.ServiceContext.TICKET_CONTEXT)
                .build();

        GlobalUtils.showLog(MQTT_LOG, "publish draw maximize");
        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawMinimize(String userAccountId, String pictureId, String accountName, String accountPicture,
                                    long orderId, long eventTime, String rtcContext) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.DrawMinize drawMinize = SignalingProto.DrawMinize.newBuilder()
                .setEventTime(eventTime)
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(pictureId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_MINIMIZE_REQUEST)
                .setDrawMinimizeRequest(drawMinize)
                .setContext(rtcContext.equals(RTC_CONTEXT_SERVICE_REQUEST) ?
                        AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT :
                        AnydoneProto.ServiceContext.TICKET_CONTEXT)
                .build();

        GlobalUtils.showLog(MQTT_LOG, "publish draw minimize");
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

    public void sendMqttLog(String eventName, boolean ownResponse) {
        if (false)
            getView().onMqttReponseArrived(eventName, ownResponse);
    }

}
