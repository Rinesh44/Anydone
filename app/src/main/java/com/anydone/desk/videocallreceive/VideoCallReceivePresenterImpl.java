package com.anydone.desk.videocallreceive;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.NotificationProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.NotificationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.mqtt.TreeleafMqttCallback;
import com.anydone.desk.mqtt.TreeleafMqttClient;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Conversation;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.ConversationRepo;
import com.anydone.desk.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivityRepository;
import com.anydone.desk.utils.GlobalUtils;
import com.treeleaf.anydone.rpc.TicketNotificationRpcProto;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.CANCEL_DRAWING_MESSAGE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_CLOSE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_COLLAB_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_MAXIMIZE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_MINIMIZE_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.DRAW_START_RESPONSE;
import static com.treeleaf.anydone.entities.RtcProto.RelayResponse.RelayResponseType.MAX_DRAWING_EXCEED;
import static com.anydone.desk.utils.Constants.MQTT_LOG;
import static com.anydone.desk.utils.Constants.RTC_CONTEXT_INBOX;
import static com.anydone.desk.utils.Constants.RTC_CONTEXT_SERVICE_REQUEST;
import static com.anydone.desk.utils.Constants.RTC_CONTEXT_TICKET;
import static com.treeleaf.januswebrtc.Const.SHOW_MQTT_LOG;

public class VideoCallReceivePresenterImpl extends
        BasePresenter<VideoCallReceiveContract.VideoCallReceiveActivityView> implements
        VideoCallReceiveContract.VideoCallReceiveActivityPresenter {
    private static final String TAG = "ServiceRequestDetailActivityPresenterImpl";
    public final String PUBLISH_TOPIC = "anydone/rtc/relay";
    private ServiceRequestDetailActivityRepository serviceRequestDetailActivityRepository;
    private Account account = AccountRepo.getInstance().getAccount();

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
                                        drawStartResponse.getImageId(), drawStartResponse.getDrawMetaData().getSenderAccount().getFullName());
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
                                .POINTER_START_RESPONSE)) {
                            SignalingProto.PointerStart pointer = relayResponse
                                    .getPointer();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + pointer.getSenderAccount().getAccountId());
                            if (pointer != null &&
                                    !pointer.getSenderAccount().getAccountId().equals(userAccountId)) {
                                getView().onDrawPointerClicked(pointer.getX(),
                                        pointer.getY(),
                                        pointer.getSenderAccount().getAccountId(),
                                        pointer.getImageId(), pointer.getSenderAccount().getFullName());
                            }
                            sendMqttLog("POINTER CLICKED", pointer.getSenderAccount().getAccountId().
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
                                    if (drawCollabResponse.getToAccountId().equals("ALL_PARTICIPANTS") ||
                                            drawCollabResponse.getToAccountId().equals(userAccountId)) {
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

                        if (relayResponse.getResponseType().equals(MAX_DRAWING_EXCEED)) {
                            SignalingProto.MaxDrawingExceed maxDrawingExceed = relayResponse.getMaxDrawingExceed();
                            String accountId = maxDrawingExceed.getSenderAccount().getAccountId();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + accountId);
                            if (maxDrawingExceed != null) {
                                if (userAccountId.equals(accountId)) {
                                    //sent and received id is same

                                } else {
                                    //sent and received id is different
                                    getView().onDrawMaxDrawingExceed(maxDrawingExceed);
                                }
                                sendMqttLog("MAX DRAWING EXCEED", maxDrawingExceed.getSenderAccount().getAccountId().
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
        Log.d(MQTT_LOG, "unsubscribe drawing mqtt");
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
    public void fetchCallerDetailsInbox(String authToken, String fcmToken, String accountId, String mCallerContext) {
        serviceRequestDetailActivityRepository.fetchCallerDetailsInbox(authToken, fcmToken)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<NotificationRpcProto.NotificationBaseResponse>() {
                    @Override
                    public void onNext(NotificationRpcProto.NotificationBaseResponse notificationBaseResponse) {
                        String refId = notificationBaseResponse.getRefId();
                        NotificationProto.Notification notification = notificationBaseResponse.getNotification();
                        try {
                            String notificationPayload = notification.getPayload();
                            Log.d("notificationpayload", "notificationPayload" + notificationPayload);

                            if (notificationPayload.isEmpty()) {
                                getView().onCallerDetailFetchFail("Caller Info response is empty!!");
                                return;
                            }

                            JSONObject payload = new JSONObject(notification.getPayload());
                            JSONObject broadcastVideoCall = payload.optJSONObject("broadcastVideoCall");

                            if (broadcastVideoCall == null) {
                                getView().onCallerDetailFetchFail("Broadcast info not available");
                                return;
                            }

                            String senderAccountId = broadcastVideoCall.optString("senderAccountId");
                            if (senderAccountId.equals(accountId)) {
                                getView().onCallerDetailFetchFail("same account id");
                                return;
                            }

                            JSONArray recipients = broadcastVideoCall.optJSONArray("recipients");
                            if (recipients.length() < 2) {
                                getView().onCallerDetailFetchFail("participants less than 2");
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            getView().onCallerDetailFetchFail(e.getLocalizedMessage());
                        }

                        getView().onCallerDetailsFetchSuccess(notification);

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onCallerDetailFetchFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    public void fetchCallerDetailsTicket(String authToken, String fcmToken, String accountId, String mCallerContext) {
        serviceRequestDetailActivityRepository.fetchCallerDetailsTickets(authToken, fcmToken)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<TicketNotificationRpcProto.TicketNotificationBaseResponse>() {
                    @Override
                    public void onNext(TicketNotificationRpcProto.TicketNotificationBaseResponse ticketNotificationBaseResponse) {
                        NotificationProto.Notification notification = ticketNotificationBaseResponse.getNotification();
                        try {
                            String notificationPayload = notification.getPayload();
                            Log.d("notificationpayload", "notificationPayload" + notificationPayload);

                            if (notificationPayload.isEmpty()) {
                                getView().onCallerDetailFetchFail("Caller Info response is empty!!");
                                return;
                            }

                            JSONObject payload = new JSONObject(notification.getPayload());
                            String notificationType = payload.optString("notificationType");
                            JSONObject notificationPayloadJson = null;
                            if (notificationType.equals("BROADCAST_VIDEO_CALL")) {
                                notificationPayloadJson = payload.optJSONObject("broadcastVideoCall");
                            } else if (notificationType.equals("ADD_CALL_PARTICIPANT")) {
                                notificationPayloadJson = payload.optJSONObject("addCallParticipant");
                            }

                            if (notificationPayloadJson == null) {
                                getView().onCallerDetailFetchFail("Broadcast info not available");
                                return;
                            }

                            String senderAccountId = notificationPayloadJson.optString("senderAccountId");
                            if (senderAccountId.equals(accountId)) {
                                getView().onCallerDetailFetchFail("same account id");
                                return;
                            }

                            JSONArray recipients = notificationPayloadJson.optJSONArray("recipients");
                            if (recipients.length() < 2) {
                                getView().onCallerDetailFetchFail("participants less than 2");
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            getView().onCallerDetailFetchFail(e.getLocalizedMessage());
                        }

                        getView().onCallerDetailsFetchSuccess(notification);

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onCallerDetailFetchFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    public void fetchCallEndDetails(String authToken, String fcmToken) {


    }

    @Override
    public void publishVideoBroadCastMessage(String userAccountId, String accountName, String accountPicture, String orderId,
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
                .setContext(getRTCContext(rtcContext))
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish response raw: " + message);
            }
        });
    }

    @Override
    public void publishAddParticipantsToCallMessage(String userAccountId, String accountName, String accountPicture,
                                                    ArrayList<String> selectedParticipantsIds, String orderId,
                                                    String sessionId, String roomId, String participantId, String janusBaseUrl,
                                                    String apiSecret, String apiKey, String rtcContext, String rtcMessageId) {
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

        SignalingProto.AddCallParticipant broadcastVideoCall = SignalingProto.AddCallParticipant.newBuilder()
                .setSessionId(sessionId == null ? "" : sessionId)
                .setRoomId(roomId)
                .setParticipantId(participantId)
                .setAvConnectDetails(avConnectDetails)
                .setClientId(clientId)
                .setSenderAccountId(userAccountId)
                .addAllAccountIds(selectedParticipantsIds)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.ADD_CALL_PARTICIPANT)
                .setAddCallParticipant(broadcastVideoCall)
                .setContext(getRTCContext(rtcContext))
                .build();

        GlobalUtils.showLog(MQTT_LOG, "publish add participant to call");
        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish response raw: " + message);
            }
        });

    }

    @Override
    public void publishHostHangUpEvent(String userAccountId, String accountName, String accountPicture,
                                       String orderId, String rtcMesssageId, boolean videoBroadCastPublish,
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
                    .setContext(getRTCContext(rtcContext))
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
                                           String orderId, String rtcContext, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.VIDEO_CALL_JOIN_REQUEST)
                .setVideoCallJoinRequest(videoCallJoinRequest)
                .setContext(getRTCContext(rtcContext))
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
                                            String orderId, String rtcContext, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.PARTICIPANT_LEFT_REQUEST)
                .setParticipantLeftRequest(participantLeft)
                .setContext(getRTCContext(rtcContext))
                .build();


        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishCallDeclineEvent(String userAccountId, String accountName, String accountPicture, String orderId, String rtcContext, String rtcMessageId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.ReceiverCallDeclined receiverCallDeclined = SignalingProto.ReceiverCallDeclined.newBuilder()
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RECEIVER_CALL_DECLINED_REQUEST)
                .setReceiverCallDeclinedRequest(receiverCallDeclined)
                .setContext(getRTCContext(rtcContext))
                .build();


        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish call decline " + message);
            }
        });
    }

    @Override
    public void publishCancelDrawEvent(String userAccountId, String accountName, String accountPicture,
                                       String orderId, long cancellationTime, String rtcContext, String imageId, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.CANCEL_DRAWING_MESSAGE_REQUEST)
                .setCancelDrawRequest(cancelDrawing)
                .setContext(getRTCContext(rtcContext))
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
                                          String orderId, Float x, Float y, CaptureDrawParam captureDrawParam,
                                          long capturedTime, String rtcContext, String imageId, String touchSessionId, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_START_REQUEST)
                .setDrawStartRequest(drawStart)
                .setContext(getRTCContext(rtcContext))
                .build();

        GlobalUtils.showLog(MQTT_LOG, "publish draw start");
        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawTouchMoveEvent(String userAccountId, String accountName, String accountPicture,
                                          String orderId, CaptureDrawParam captureDrawParam, Float prevX, Float prevY,
                                          long capturedTime, String rtcContext, String imageId, String touchSessionId, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_TOUCH_MOVE_REQUEST)
                .setDrawTouchMoveRequest(drawTouchMove)
                .setContext(getRTCContext(rtcContext))
                .build();

        GlobalUtils.showLog(MQTT_LOG, "publish draw move");
        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawTouchUpEvent(String userAccountId, String accountName, String accountPicture,
                                        String orderId, long capturedTime, String rtcContext, String imageId, String touchSessionId, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_END_REQUEST)
//                .setDrawTouchUpRequest(drawTouchUp)
                .setDrawEndRequest(drawEnd)
                .setContext(getRTCContext(rtcContext))
                .build();

        GlobalUtils.showLog(MQTT_LOG, "publish draw end");
        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishDrawCanvasClearEvent(String userAccountId, String accountName, String accountPicture,
                                            String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_CANVAS_CLEAR_REQUEST)
                .setDrawCanvasClearRequest(drawCanvasClear)
                .setContext(getRTCContext(rtcContext))
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
                                               Float x, Float y, String textFieldId, String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId, CaptureDrawParam captureDrawParam) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.RECEIVE_NEW_TEXT_FIELD_REQUEST)
                .setReceiveNewTextFieldRequest(receiveNewTextField)
                .setContext(getRTCContext(rtcContext))
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
                                                 String text, String textFieldId, String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.TEXT_FIELD_CHANGE_REQUEST)
                .setTextFieldChangeRequest(textFieldChange)
                .setContext(getRTCContext(rtcContext))
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
                                                 String textFieldId, String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.TEXT_FIELD_REMOVE_REQUEST)
                .setTextFieldRemoveRequest(textFieldRemove)
                .setContext(getRTCContext(rtcContext))
                .build();

        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void publishPointerClickEvent(String userAccountId, String accountName, String accountPicture, Float x, Float y,
                                         String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.PointerStart pointerStart = SignalingProto.PointerStart.newBuilder()
                .setX(x)
                .setY(y)
                .setEventTime(capturedTime)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setImageId(imageId)
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.POINTER_START_REQUEST)
                .setPointer(pointerStart)
                .setContext(getRTCContext(rtcContext))
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
                                             String accountName, String accountPicture, String orderId,
                                             ByteString capturedImage, long capturedTime, String rtcContext, String rtcMessageId) {
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
                .setToAccountId(toAccountId)
                .setSenderAccount(account)
                .setImageId(pictureId)
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_COLLAB_REQUEST)
                .setDrawCollabReq(drawCollab)
                .setContext(getRTCContext(rtcContext))
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
                                    String orderId, long eventTime, String rtcContext, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_MAXIMIZE_REQUEST)
                .setDrawMaximizeReq(drawMaximize)
                .setContext(getRTCContext(rtcContext))
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
                                    String orderId, long eventTime, String rtcContext, String rtcMessageId) {
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
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.DRAW_MINIMIZE_REQUEST)
                .setDrawMinimizeRequest(drawMinize)
                .setContext(getRTCContext(rtcContext))
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
    public void publishMaxDrawExceed(String userAccountId, String accountName, String accountPicture,
                                     String orderId, long eventTime, String rtcContext, String rtcMessageId) {
        String clientId = UUID.randomUUID().toString().replace("-", "");

        UserProto.Account account = UserProto.Account.newBuilder()
                .setAccountId(userAccountId)
                .setFullName(accountName)
                .setProfilePic(accountPicture)
                .build();

        SignalingProto.MaxDrawingExceed maxDrawingExceed = SignalingProto.MaxDrawingExceed.newBuilder()
                .setEventTime(eventTime)
                .setSenderAccountId(userAccountId)
                .setClientId(clientId)
                .setRefId(String.valueOf(orderId))
                .setSenderAccount(account)
                .setRtcMessageId(rtcMessageId == null ? "" : rtcMessageId)
                .build();

        RtcProto.RelayRequest relayRequest = RtcProto.RelayRequest.newBuilder()
                .setRelayType(RtcProto.RelayRequest.RelayRequestType.MAX_DRAWING_EXCEED)
                .setMaxDrawingExceed(maxDrawingExceed)
                .setContext(getRTCContext(rtcContext))
                .build();

        GlobalUtils.showLog(MQTT_LOG, "publish max draw exceed");
        TreeleafMqttClient.publish(PUBLISH_TOPIC, relayRequest.toByteArray(), new TreeleafMqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                GlobalUtils.showLog(TAG, "publish host left: " + message);
            }
        });
    }

    @Override
    public void subscribeSuccessMessageAVCall(String ticketId, String userAccountId) throws MqttException {
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

                        }

                        if (relayResponse.getResponseType().equals(RtcProto.RelayResponse.RelayResponseType
                                .ADD_CALL_PARTICIPANT)) {
                            SignalingProto.AddCallParticipant addCallParticipant =
                                    relayResponse.getAddCallParticipant();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + addCallParticipant.getSenderAccountId());
                            if (addCallParticipant != null) {
                                if (!userAccountId.equals(addCallParticipant.getSenderAccountId())) {
                                    if (addCallParticipant.getAccountIdsList().contains(userAccountId)) {
                                    }
                                } else if (userAccountId.equals(addCallParticipant.getSenderAccountId())) {
                                    Toast.makeText(getContext(), "Selected participants invited to this call",
                                            Toast.LENGTH_LONG).show();
                                }
                                sendMqttLog("ADD_CALL_PARTICIPANT", userAccountId.equals(addCallParticipant.getSenderAccountId()));
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
                            GlobalUtils.showLog(TAG, "host left");
                            SignalingProto.VideoRoomHostLeft videoRoomHostLeft = relayResponse
                                    .getVideoRoomHostLeftResponse();
                            GlobalUtils.showLog(MQTT_LOG, relayResponse.getResponseType() + " from " + videoRoomHostLeft.getSenderAccount().getAccountId());
                            GlobalUtils.showLog(TAG, "user id check: " + userAccountId);
                        /*    if (!userAccountId.equals(videoRoomHostLeft.getSenderAccount().getAccountId())) {
                                getView().onHostHangUp(videoRoomHostLeft);
                            }*/
                            getView().onHostHangUp(videoRoomHostLeft);
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
                            getView().onCallDeclined(receiverCallDeclined);
                            sendMqttLog("CALL_DECLINED", receiverCallDeclined.getSenderAccount().getAccountId().
                                    equals(userAccountId));
                        }

                    }
                }
            }

        });
    }

    @Override
    public void subscribeFailMessageAVCall(String refId) throws MqttException {
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
//                    setConversationAsFailed(conversation);//TODO: ask rinesh how to fix this later
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
    public void checkConnection(MqttAndroidClient client) {
        if (!GlobalUtils.isConnected(getContext())) {
            getView().onConnectionFail("No Internet Connection");
        } else if (!client.isConnected()) {
            getView().onConnectionFail("Reconnecting...");
        } else {
            getView().onConnectionSuccess();
        }
    }

    public void sendMqttLog(String eventName, boolean ownResponse) {
        if (SHOW_MQTT_LOG)
            getView().onMqttReponseArrived(eventName, ownResponse);
    }

    private AnydoneProto.ServiceContext getRTCContext(String contextType) {
        switch (contextType) {
            case RTC_CONTEXT_SERVICE_REQUEST:
                return AnydoneProto.ServiceContext.SERVICE_ORDER_CONTEXT;
            case RTC_CONTEXT_TICKET:
                return AnydoneProto.ServiceContext.TICKET_CONTEXT;
            case RTC_CONTEXT_INBOX:
                return AnydoneProto.ServiceContext.INBOX_CONTEXT;
            default:
                return AnydoneProto.ServiceContext.TICKET_CONTEXT;
        }
    }

}
