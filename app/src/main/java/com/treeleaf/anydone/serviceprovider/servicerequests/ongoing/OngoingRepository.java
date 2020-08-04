package com.treeleaf.anydone.serviceprovider.servicerequests.ongoing;

import com.treeleaf.anydone.rpc.OrderServiceRpcProto;

import io.reactivex.Observable;

public interface OngoingRepository {
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> cancelOrder(String token,
                                                                          long orderId);
}
