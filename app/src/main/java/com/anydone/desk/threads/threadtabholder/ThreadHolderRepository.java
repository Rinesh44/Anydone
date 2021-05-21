package com.anydone.desk.threads.threadtabholder;

import com.treeleaf.anydone.rpc.ServiceRpcProto;

import io.reactivex.Observable;

public interface ThreadHolderRepository {
    Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token);

}
