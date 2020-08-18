package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;


import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class ServiceRequestDetailActivityContract {

    public interface ServiceRequestDetailActivityView extends BaseView {

        void onUrlFetchSuccess(String janusBaseUrl, String apiKey, String apiSecret);

        void onUrlFetchFail(String msg);

        void onConnectionSuccess();

        void onConnectionFail(String msg);

    }

    public interface ServiceRequestDetailActivityPresenter extends Presenter<ServiceRequestDetailActivityView> {

        void fetchJanusServerUrl(String token);

        void publishVideoBroadCastMessage(String userAccountId, String accountName, String accountPicture, long orderId,
                                          String sessionId, String roomId, String participantId,
                                          String janusBaseUrl, String apiSecret, String apiKey);

        void checkConnection(MqttAndroidClient client);

        void publishHostHangUpEvent(String userAccountId, String accountName, String accountPicture, long orderId, String rtcMesssageId, boolean videoBroadCastPublish);

        void publishSubscriberJoinEvent(String userAccountId, String accountName, String accountPicture,
                                        long orderId);

        void publishParticipantLeftEvent(String userAccountId, String accountName, String accountPicture,
                                         long orderId);

    }

}
