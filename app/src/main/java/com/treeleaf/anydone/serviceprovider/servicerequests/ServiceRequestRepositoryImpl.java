package com.treeleaf.anydone.serviceprovider.servicerequests;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;

import io.reactivex.Observable;

public class ServiceRequestRepositoryImpl implements ServiceRequestRepository {
    AnyDoneService service;

    public ServiceRequestRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getOrderService(String token) {
        return service.getOrderService(token);
    }

    @Override
    public Observable<OrderServiceRpcProto.OrderServiceBaseResponse> filterServiceRequests
            (String token, String serviceName, long from, long to, String status) {
        return service.filterServiceRequests(token, serviceName, from, to, status);
    }
}
