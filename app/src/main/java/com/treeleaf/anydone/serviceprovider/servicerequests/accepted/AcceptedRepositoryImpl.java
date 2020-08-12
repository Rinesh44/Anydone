package com.treeleaf.anydone.serviceprovider.servicerequests.accepted;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;

import io.reactivex.Observable;

public class AcceptedRepositoryImpl implements AcceptedRepository {
    AnyDoneService anyDoneService;

    public AcceptedRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<OrderServiceRpcProto.OrderServiceBaseResponse> cancelOrder(String token, long
            orderId) {
        return anyDoneService.cancelOrder(token, orderId);
    }
}
