package com.treeleaf.anydone.serviceprovider.reply;

import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class ReplyRepositoryImpl implements ReplyRepository {
    AnyDoneService anyDoneService;

    public ReplyRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<InboxRpcProto.InboxBaseResponse> getReplies(String msgId) {
        return null;
    }
}
