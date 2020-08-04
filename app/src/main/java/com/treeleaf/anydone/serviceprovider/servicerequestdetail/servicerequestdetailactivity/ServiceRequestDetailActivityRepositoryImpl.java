package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;


import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;

import io.reactivex.Observable;

public class ServiceRequestDetailActivityRepositoryImpl implements ServiceRequestDetailActivityRepository {

    AnyDoneService service;

    public ServiceRequestDetailActivityRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> fetchJanusServerUrl(String token) {
        return service.getJanusBaseUrl(token);
    }
}
