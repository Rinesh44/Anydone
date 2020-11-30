package com.treeleaf.anydone.serviceprovider.videocallreceive;

import android.text.TextUtils;

import com.google.protobuf.ByteString;
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

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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
                                          long orderId, Float x, Float y, long capturedTime, String rtcContext, String imageId) {
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
                .setImageId(imageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_TOUCH_DOWN_REQUEST)
                .setDrawTouchDownRequest(drawTouchDown)
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
                                          long orderId, Float x, Float y, long capturedTime, String rtcContext, String imageId) {
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
                .setImageId(imageId)
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
                                        long orderId, long capturedTime, String rtcContext, String imageId) {
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
                .setImageId(imageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_TOUCH_UP_REQUEST)
                .setDrawTouchUpRequest(drawTouchUp)
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
                                               Float x, Float y, String textFieldId, long orderId, long capturedTime, String rtcContext, String imageId) {
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
                .setImageId(imageId)
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
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_COLlAB_REQUEST)
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

}
