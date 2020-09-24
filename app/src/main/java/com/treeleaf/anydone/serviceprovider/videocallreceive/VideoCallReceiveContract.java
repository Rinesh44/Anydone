package com.treeleaf.anydone.serviceprovider.videocallreceive;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import com.google.protobuf.ByteString;


public class VideoCallReceiveContract {

    public interface VideoCallReceiveActivityView extends BaseView {

    }

    public interface VideoCallReceiveActivityPresenter extends Presenter<VideoCallReceiveActivityView> {


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
                                    long orderId, long cancellationTime, String rtcContext);

        void publishDrawTouchDownEvent(String userAccountId, String accountName, String accountPicture,
                                       long orderId, Float x, Float y, long capturedTime, String rtcContext);

        void publishDrawTouchMoveEvent(String userAccountId, String accountName, String accountPicture,
                                       long orderId, Float x, Float y, long capturedTime, String rtcContext);

        void publishDrawTouchUpEvent(String userAccountId, String accountName, String accountPicture,
                                     long orderId, long capturedTime, String rtcContext);

        void publishDrawMetaChangeEvent(String userAccountId, String accountName, String accountPicture,
                                        Float x, Float y, Float brushWidth, Float brushOpacity,
                                        int brushColor, int textColor, long orderId, long capturedTime, String rtcContext);

        void publishDrawCanvasClearEvent(String userAccountId, String accountName, String accountPicture,
                                         long orderId, long capturedTime, String rtcContext);

        void publishDrawReceiveNewTextEvent(String userAccountId, String accountName, String accountPicture,
                                            Float x, Float y, String textFieldId, long orderId, long capturedTime, String rtcContext);

        void publishTextFieldChangeEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String text, String textFieldId, long orderId, long capturedTime, String rtcContext);

        void publishTextFieldRemoveEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String textFieldId, long orderId, long capturedTime, String rtcContext);

    }

}
