package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class ServiceRequestDetailActivityPresenterImpl extends
        BasePresenter<ServiceRequestDetailActivityContract.ServiceRequestDetailActivityView> implements
        ServiceRequestDetailActivityContract.ServiceRequestDetailActivityPresenter {

    private static final String TAG = "ServiceRequestDetailActivityPresenterImpl";
    public final String PUBLISH_TOPIC = "anydone/rtc/relay";
    private ServiceRequestDetailActivityRepository serviceRequestDetailActivityRepository;

    @Inject
    public ServiceRequestDetailActivityPresenterImpl(ServiceRequestDetailActivityRepository serviceRequestDetailActivityRepository) {
        this.serviceRequestDetailActivityRepository = serviceRequestDetailActivityRepository;
    }

}