package com.treeleaf.anydone.serviceprovider.videocallreceive;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import com.google.protobuf.ByteString;

import org.eclipse.paho.android.service.MqttAndroidClient;


public class VideoCallReceiveContract {

    public interface VideoCallReceiveActivityView extends BaseView {

        void onUrlFetchSuccess(String janusBaseUrl, String apiKey, String apiSecret);

        void onUrlFetchFail(String msg);

        void onConnectionSuccess();

        void onConnectionFail(String msg);

    }

    public interface VideoCallReceiveActivityPresenter extends Presenter<VideoCallReceiveActivityView> {

        void fetchJanusServerUrl(String token);

        void publishVideoBroadCastMessage(String userAccountId, String accountName, String accountPicture, long orderId,
                                          String sessionId, String roomId, String participantId,
                                          String janusBaseUrl, String apiSecret, String apiKey, String rtcContext);

        void publishHostHangUpEvent(String userAccountId, String accountName, String accountPicture,
                                    long orderId, String rtcMesssageId, boolean videoBroadCastPublish, String rtcContext);

        void checkConnection(MqttAndroidClient client);

        void publishSubscriberJoinEvent(String userAccountId, String accountName, String accountPicture,
                                        long orderId, String rtcContext);

        void publishParticipantLeftEvent(String userAccountId, String accountName, String accountPicture,
                                         long orderId, String rtcContext);

        void publishSendImageToRemoteEvent(String userAccountId, String accountName, String accountPicture,
                                           long orderId, ByteString capturedImage, int bitmapWidth, int bitmapHeight,
                                           long capturedTime, String rtcContext);

        void publishSendAckToRemoteEvent(String userAccountId, String accountName, String accountPicture,
                                         long orderId, int bitmapWidth, int bitmapHeight,
                                         long capturedTime, String rtcContext);

        void publishCancelDrawEvent(String userAccountId, String accountName, String accountPicture,
                                    long orderId, long cancellationTime, String rtcContext, String imageId);

        void publishDrawTouchDownEvent(String userAccountId, String accountName, String accountPicture,
                                       long orderId, Float x, Float y, long capturedTime, String rtcContext, String imageId);

        void publishDrawTouchMoveEvent(String userAccountId, String accountName, String accountPicture,
                                       long orderId, Float x, Float y, long capturedTime, String rtcContext, String imageId);

        void publishDrawTouchUpEvent(String userAccountId, String accountName, String accountPicture,
                                     long orderId, long capturedTime, String rtcContext, String imageId);

        void publishDrawMetaChangeEvent(String userAccountId, String accountName, String accountPicture,
                                        Float x, Float y, Float brushWidth, Float brushOpacity,
                                        int brushColor, int textColor, long orderId, long capturedTime, String rtcContext, String imageId);

        void publishDrawCanvasClearEvent(String userAccountId, String accountName, String accountPicture,
                                         long orderId, long capturedTime, String rtcContext, String imageId);

        void publishDrawReceiveNewTextEvent(String userAccountId, String accountName, String accountPicture,
                                            Float x, Float y, String textFieldId, long orderId, long capturedTime, String rtcContext, String imageId);

        void publishTextFieldChangeEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String text, String textFieldId, long orderId, long capturedTime, String rtcContext, String imageId);

        void publishTextFieldRemoveEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String textFieldId, long orderId, long capturedTime, String rtcContext, String imageId);


        void publishInviteToCollabRequest(String fromAccountId, String toAccountId, String pictureId, String accountName, String accountPicture,
                                          long orderId, ByteString capturedImage, long capturedTime, String rtcContext);

        void publishDrawMaximize(String userAccountId, String pictureId, String accountName, String accountPicture,
                                 long orderId, long eventTime, String rtcContext);

        void publishDrawMinimize(String userAccountId, String pictureId, String accountName, String accountPicture,
                                 long orderId, long eventTime, String rtcContext);


    }

}