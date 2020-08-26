package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;


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

    }

}
