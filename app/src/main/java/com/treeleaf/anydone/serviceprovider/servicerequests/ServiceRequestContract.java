package com.treeleaf.anydone.serviceprovider.servicerequests;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.FilterObject;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;

import java.util.List;

public class ServiceRequestContract {
    public interface ServiceRequestView extends BaseView {

        void setServiceNames(List<FilterObject> serviceNames);

        void setFromDateTime(long fromTime);

        void setTillDateTime(long tillTime);

        void getServiceRequestSuccess();

        void getServiceRequestFail(String msg);

        void onFilterRequestsSuccess(List<ServiceRequest> filteredServiceRequests);

        void onFilterRequestsFail(String msg);

        void onOngoingRequestTypeSeparated(List<ServiceRequest> onGoingRequestList);

        void onClosedRequestTypeSeparated(List<ServiceRequest> closedRequestList);

    }

    public interface ServiceRequestPresenter extends Presenter<ServiceRequestView> {
        void getServiceNames(List<ServiceRequest> serviceList);

        void getFromDateTime(String fromDate);

        void getTillDateTime(String tillDate);

        void getServiceRequests(boolean showProgress);

        void filterServiceRequests(String serviceName, long from, long to, String status);

        void separateOngoingAndClosedRequests(List<ServiceRequest> serviceList, int fragmentIndex,
                                              boolean filter);
    }
}
