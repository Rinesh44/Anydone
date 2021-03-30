package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;

import com.treeleaf.anydone.rpc.NotificationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public class ServiceRequestDetailActivityRepositoryImpl implements ServiceRequestDetailActivityRepository {

    AnyDoneService service;

    public ServiceRequestDetailActivityRepositoryImpl(AnyDoneService service1) {
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        service = retrofit.create(AnyDoneService.class);
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> fetchJanusServerUrl(String token) {
        return service.getJanusBaseUrl(token);
    }

    @Override
    public Observable<NotificationRpcProto.NotificationBaseResponse> fetchCallerDetails(String authToken, String fcmToken) {
        return service.getCallerDetails(authToken, fcmToken);
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> fetchCallEndDetails(String authToken, String fcmToken) {
        return service.getCallEndDetails(authToken, fcmToken);
    }
}