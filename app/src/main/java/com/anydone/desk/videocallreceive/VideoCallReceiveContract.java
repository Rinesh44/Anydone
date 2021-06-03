package com.anydone.desk.videocallreceive;

import com.treeleaf.anydone.entities.NotificationProto;
import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

import com.google.protobuf.ByteString;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;


public class VideoCallReceiveContract {

    public interface VideoCallReceiveActivityView extends BaseView, OnVideoCallEventListener {

        void onUrlFetchSuccess(String janusBaseUrl, String apiKey, String apiSecret);

        void onCallerDetailsFetchSuccess(NotificationProto.Notification callerDetails);

        void onCallEndDetailsFetchSuccess(String callEndDetails);

        void onUrlFetchFail(String msg);

        void onConnectionSuccess();

        void onConnectionFail(String msg);

        void onCallerDetailFetchFail(String localizedMessage);

    }

    public interface VideoCallReceiveActivityPresenter extends Presenter<VideoCallReceiveActivityView> {

        void subscribeSuccessMessageDrawing(String ticketId, String userAccountId) throws MqttException;

        void subscribeFailMessageDrawing(String ticketId, String userAccountId) throws MqttException;

        void unSubscribeDrawing(String ticketId, String userAccountId) throws MqttException;

        void fetchJanusServerUrl(String token);

        void publishCallBroadCastMessage(String userAccountId, String accountName, String accountPicture, String orderId,
                                         String sessionId, String roomId, String participantId,
                                         String janusBaseUrl, String apiSecret, String apiKey, String rtcContext);

        void publishAddParticipantsToCallMessage(String userAccountId, String accountName, String accountPicture,
                                                 ArrayList<String> selectedParticipantsIds,
                                                 String orderId, String sessionId, String roomId, String participantId,
                                                 String janusBaseUrl, String apiSecret, String apiKey, String rtcContext, String rtcMessageId);

        void publishHostHangUpEvent(String userAccountId, String accountName, String accountPicture,
                                    String orderId, String rtcMesssageId, boolean videoBroadCastPublish, String rtcContext);

        void checkConnection(MqttAndroidClient client);

        void publishJoinEvent(String mLocalParticipantId, String userAccountId, String accountName, String accountPicture,
                              String orderId, String rtcContext, String rtcMessageId);

        void publishParticipantLeftEvent(String userAccountId, String accountName, String accountPicture,
                                         String orderId, String rtcContext, String rtcMessageId);

        void publishCallDeclineEvent(String userAccountId, String accountName, String accountPicture,
                                     String orderId, String rtcContext, String rtcMessageId);

        void publishCancelDrawEvent(String userAccountId, String accountName, String accountPicture,
                                    String orderId, long cancellationTime, String rtcContext, String imageId, String rtcMessageId);

        void publishDrawTouchDownEvent(String userAccountId, String accountName, String accountPicture,
                                       String orderId, Float x, Float y, CaptureDrawParam captureDrawParam, long capturedTime, String rtcContext, String imageId, String touchSessionId, String rtcMessageId);

        void publishDrawTouchMoveEvent(String userAccountId, String accountName, String accountPicture,
                                       String orderId, CaptureDrawParam captureDrawParam, Float prevX, Float prevY, long capturedTime, String rtcContext, String imageId, String touchSessionId, String rtcMessageId);

        void publishDrawTouchUpEvent(String userAccountId, String accountName, String accountPicture,
                                     String orderId, long capturedTime, String rtcContext, String imageId, String touchSessionId, String rtcMessageId);

        void publishDrawCanvasClearEvent(String userAccountId, String accountName, String accountPicture,
                                         String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId);

        void publishDrawReceiveNewTextEvent(String userAccountId, String accountName, String accountPicture,
                                            Float x, Float y, String textFieldId, String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId, CaptureDrawParam captureDrawParam);

        void publishTextFieldChangeEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String text, String textFieldId, String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId);

        void publishTextFieldRemoveEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String textFieldId, String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId);

        void publishPointerClickEvent(String userAccountId, String accountName, String accountPicture,
                                      Float x, Float y, String orderId, long capturedTime, String rtcContext, String imageId, String rtcMessageId);

        void publishInviteToCollabRequest(String fromAccountId, String toAccountId, String pictureId, String accountName, String accountPicture,
                                          String orderId, ByteString capturedImage, long capturedTime, String rtcContext, String rtcMessageId);

        void publishDrawMaximize(String userAccountId, String pictureId, String accountName, String accountPicture,
                                 String orderId, long eventTime, String rtcContext, String rtcMessageId);

        void publishDrawMinimize(String userAccountId, String pictureId, String accountName, String accountPicture,
                                 String orderId, long eventTime, String rtcContext, String rtcMessageId);

        void publishMaxDrawExceed(String userAccountId, String accountName, String accountPicture,
                                  String orderId, long eventTime, String rtcContext, String rtcMessageId);

        void fetchCallerDetailsInbox(String authToken, String fcmToken, String accountId, String mCallerContext);

        void fetchCallerDetailsTicket(String authToken, String fcmToken, String accountId, String mCallerContext);

        void fetchCallEndDetails(String authToken, String fcmToken);

        void subscribeSuccessMessageAVCall(String ticketId, String userAccountId) throws MqttException;

        void subscribeFailMessageAVCall(String refId) throws MqttException;

        void unSubscribeAVCall(String ticketId, String accountId) throws MqttException;

    }

}