package com.treeleaf.anydone.serviceprovider.videocallreceive;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import com.google.protobuf.ByteString;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;


public class VideoCallReceiveContract {

    public interface VideoCallReceiveActivityView extends BaseView, OnVideoCallEventListener {

        void onUrlFetchSuccess(String janusBaseUrl, String apiKey, String apiSecret);

        void onUrlFetchFail(String msg);

        void onConnectionSuccess();

        void onConnectionFail(String msg);

    }

    public interface VideoCallReceiveActivityPresenter extends Presenter<VideoCallReceiveActivityView> {

        void subscribeSuccessMessageDrawing(String ticketId, String userAccountId) throws MqttException;

        void subscribeFailMessageDrawing(String ticketId, String userAccountId) throws MqttException;

        void unSubscribeDrawing(String ticketId, String userAccountId) throws MqttException;

        void fetchJanusServerUrl(String token);

        void publishVideoBroadCastMessage(String userAccountId, String accountName, String accountPicture, String orderId,
                                          String sessionId, String roomId, String participantId,
                                          String janusBaseUrl, String apiSecret, String apiKey, String rtcContext);

        void publishHostHangUpEvent(String userAccountId, String accountName, String accountPicture,
                                    String orderId, String rtcMesssageId, boolean videoBroadCastPublish, String rtcContext);

        void checkConnection(MqttAndroidClient client);

        void publishSubscriberJoinEvent(String userAccountId, String accountName, String accountPicture,
                                        String orderId, String rtcContext);

        void publishParticipantLeftEvent(String userAccountId, String accountName, String accountPicture,
                                         String orderId, String rtcContext);

        void publishSendImageToRemoteEvent(String userAccountId, String accountName, String accountPicture,
                                           long orderId, ByteString capturedImage, int bitmapWidth, int bitmapHeight,
                                           long capturedTime, String rtcContext);

        void publishSendAckToRemoteEvent(String userAccountId, String accountName, String accountPicture,
                                         long orderId, int bitmapWidth, int bitmapHeight,
                                         long capturedTime, String rtcContext);

        void publishCancelDrawEvent(String userAccountId, String accountName, String accountPicture,
                                    String orderId, long cancellationTime, String rtcContext, String imageId);

        void publishDrawTouchDownEvent(String userAccountId, String accountName, String accountPicture,
                                       String orderId, Float x, Float y, CaptureDrawParam captureDrawParam, long capturedTime, String rtcContext, String imageId, String touchSessionId);

        void publishDrawTouchMoveEvent(String userAccountId, String accountName, String accountPicture,
                                       String orderId, CaptureDrawParam captureDrawParam, Float prevX, Float prevY, long capturedTime, String rtcContext, String imageId, String touchSessionId);

        void publishDrawTouchUpEvent(String userAccountId, String accountName, String accountPicture,
                                     String orderId, long capturedTime, String rtcContext, String imageId, String touchSessionId);

        void publishDrawMetaChangeEvent(String userAccountId, String accountName, String accountPicture,
                                        Float x, Float y, Float brushWidth, Float brushOpacity,
                                        int brushColor, int textColor, long orderId, long capturedTime, String rtcContext, String imageId);

        void publishDrawCanvasClearEvent(String userAccountId, String accountName, String accountPicture,
                                         String orderId, long capturedTime, String rtcContext, String imageId);

        void publishDrawReceiveNewTextEvent(String userAccountId, String accountName, String accountPicture,
                                            Float x, Float y, String textFieldId, String orderId, long capturedTime, String rtcContext, String imageId, CaptureDrawParam captureDrawParam);

        void publishTextFieldChangeEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String text, String textFieldId, String orderId, long capturedTime, String rtcContext, String imageId);

        void publishTextFieldRemoveEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String textFieldId, String orderId, long capturedTime, String rtcContext, String imageId);


        void publishInviteToCollabRequest(String fromAccountId, String toAccountId, String pictureId, String accountName, String accountPicture,
                                          String orderId, ByteString capturedImage, long capturedTime, String rtcContext);

        void publishDrawMaximize(String userAccountId, String pictureId, String accountName, String accountPicture,
                                 String orderId, long eventTime, String rtcContext);

        void publishDrawMinimize(String userAccountId, String pictureId, String accountName, String accountPicture,
                                 String orderId, long eventTime, String rtcContext);


    }

}