package com.anydone.desk.threads;

import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class ThreadRepositoryImpl implements ThreadRepository {
    AnyDoneService service;

    public ThreadRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<ConversationRpcProto.ConversationBaseResponse> getConversationThreads(String token, String serviceId) {
        return service.getConversationThreads(token, serviceId);
    }

    @Override
    public Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token) {
        return service.getServices(token);
    }
}
