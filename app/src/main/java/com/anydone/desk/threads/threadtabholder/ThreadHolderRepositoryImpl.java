package com.anydone.desk.threads.threadtabholder;

import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class ThreadHolderRepositoryImpl implements ThreadHolderRepository {
    AnyDoneService anyDoneService;

    public ThreadHolderRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token) {
        return null;
    }
}
