package com.treeleaf.anydone.serviceprovider.threads.threadtabholder;

import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

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
