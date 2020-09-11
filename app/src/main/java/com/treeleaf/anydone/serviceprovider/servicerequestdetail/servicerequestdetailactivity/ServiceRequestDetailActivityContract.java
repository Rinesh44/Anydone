package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;


import com.google.protobuf.ByteString;
import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class ServiceRequestDetailActivityContract {

    public interface ServiceRequestDetailActivityView extends BaseView {

    }

    public interface ServiceRequestDetailActivityPresenter extends Presenter<ServiceRequestDetailActivityView> {


        void publishSubscriberJoinEvent(String userAccountId, String accountName, String accountPicture,
                                        long orderId);

        void publishParticipantLeftEvent(String userAccountId, String accountName, String accountPicture,
                                         long orderId);

        void publishSendImageToRemoteEvent(String userAccountId, String accountName, String accountPicture,
                                           long orderId, ByteString capturedImage, int bitmapWidth, int bitmapHeight,
                                           long capturedTime);

        void publishSendAckToRemoteEvent(String userAccountId, String accountName, String accountPicture,
                                         long orderId, int bitmapWidth, int bitmapHeight,
                                         long capturedTime);

        void publishCancelDrawEvent(String userAccountId, String accountName, String accountPicture,
                                    long orderId, long cancellationTime);

        void publishDrawTouchDownEvent(String userAccountId, String accountName, String accountPicture,
                                       long orderId, Float x, Float y, long capturedTime);

        void publishDrawTouchMoveEvent(String userAccountId, String accountName, String accountPicture,
                                       long orderId, Float x, Float y, long capturedTime);

        void publishDrawTouchUpEvent(String userAccountId, String accountName, String accountPicture,
                                     long orderId, long capturedTime);

        void publishDrawMetaChangeEvent(String userAccountId, String accountName, String accountPicture,
                                        Float x, Float y, Float brushWidth, Float brushOpacity,
                                        int brushColor, long orderId, long capturedTime);

        void publishDrawCanvasClearEvent(String userAccountId, String accountName, String accountPicture,
                                         long orderId, long capturedTime);

        void publishDrawReceiveNewTextEvent(String userAccountId, String accountName, String accountPicture,
                                            Float x, Float y, String textFieldId, long orderId, long capturedTime);

        void publishTextFieldChangeEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String text, String textFieldId, long orderId, long capturedTime);

        void publishTextFieldRemoveEventEvent(String userAccountId, String accountName, String accountPicture,
                                              String textFieldId, long orderId, long capturedTime);

    }

}
