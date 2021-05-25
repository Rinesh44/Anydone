package com.anydone.desk.servicerequestdetail.servicerequestdetailactivity;

import com.treeleaf.anydone.rpc.NotificationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.GlobalUtils;

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
    public Observable<NotificationRpcProto.NotificationBaseResponse> fetchCallerDetailsInbox(String authToken, String fcmToken) {
        return service.getCallerDetailsInbox(authToken, fcmToken);
    }

    @Override
    public Observable<NotificationRpcProto.NotificationBaseResponse> fetchCallerDetailsTickets(String authToken, String fcmToken) {
        return service.getCallerDetailsTickets(authToken, fcmToken);
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> fetchCallEndDetails(String authToken, String fcmToken) {
        return service.getCallEndDetails(authToken, fcmToken);
    }
}