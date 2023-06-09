package com.anydone.desk.inbox;

import com.treeleaf.anydone.rpc.ServiceRpcProto;

import io.reactivex.Observable;

public interface InboxRepository {
    Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token);

}
