package com.treeleaf.anydone.serviceprovider.servicerequests.ongoing;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;

import io.reactivex.Observable;

public class OngoingRepositoryImpl implements OngoingRepository {
    AnyDoneService anyDoneService;

    public OngoingRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<OrderServiceRpcProto.OrderServiceBaseResponse> cancelOrder(String token, long
            orderId) {
        return anyDoneService.cancelOrder(token, orderId);
    }
}
