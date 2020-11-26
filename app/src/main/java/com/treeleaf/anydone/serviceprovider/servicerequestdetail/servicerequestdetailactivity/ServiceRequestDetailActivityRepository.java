package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;

import com.treeleaf.anydone.rpc.RtcServiceRpcProto;

import io.reactivex.Observable;

public interface ServiceRequestDetailActivityRepository {

    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> fetchJanusServerUrl(String token);

}
