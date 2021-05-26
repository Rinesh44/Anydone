package com.anydone.desk.servicerequestdetail.servicerequestdetailactivity;

import com.treeleaf.anydone.rpc.NotificationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;

import io.reactivex.Observable;

public interface ServiceRequestDetailActivityRepository {

    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> fetchJanusServerUrl(String token);

    Observable<NotificationRpcProto.NotificationBaseResponse> fetchCallerDetailsInbox(String authToken, String fcmToken);

    Observable<NotificationRpcProto.NotificationBaseResponse> fetchCallerDetailsTickets(String authToken, String fcmToken);

    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> fetchCallEndDetails(String authToken, String fcmToken);

}
