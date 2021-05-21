package com.anydone.desk.threads;

import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;

import io.reactivex.Observable;

public interface ThreadRepository {
    Observable<ConversationRpcProto.ConversationBaseResponse> getConversationThreads(String token,
                                                                                     String serviceId);

    Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token);
}
