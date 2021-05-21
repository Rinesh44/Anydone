package com.anydone.desk.inbox;

import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class InboxRepositoryImpl implements InboxRepository {
    private AnyDoneService anydoneService;

    public InboxRepositoryImpl(AnyDoneService anyDoneService) {
        this.anydoneService = anyDoneService;
    }

    @Override
    public Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token) {
        return anydoneService.getServices(token);
    }
}
