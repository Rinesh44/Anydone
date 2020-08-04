package com.treeleaf.anydone.serviceprovider.servicerequests;

import com.treeleaf.anydone.rpc.OrderServiceRpcProto;

import io.reactivex.Observable;

public interface ServiceRequestRepository {

    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getOrderService(String token);

    Observable<OrderServiceRpcProto.OrderServiceBaseResponse>
    filterServiceRequests(String token,
                          String serviceName,
                          long from,
                          long to,
                          String status);

}
