package com.anydone.desk.forwardMessage;

import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import java.util.List;

import io.reactivex.Observable;

public class ForwardMessageRepositoryImpl implements ForwardMessageRepository {
    AnyDoneService anyDoneService;

    public ForwardMessageRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<InboxRpcProto.InboxBaseResponse> forwardMessage(List<String> participants, String msg) {
        return null;
    }
}
