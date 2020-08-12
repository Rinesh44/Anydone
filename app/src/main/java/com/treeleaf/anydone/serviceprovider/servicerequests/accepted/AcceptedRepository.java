package com.treeleaf.anydone.serviceprovider.servicerequests.accepted;

import com.treeleaf.anydone.rpc.OrderServiceRpcProto;

import io.reactivex.Observable;

public interface AcceptedRepository {
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> cancelOrder(String token,
                                                                          long orderId);
}
