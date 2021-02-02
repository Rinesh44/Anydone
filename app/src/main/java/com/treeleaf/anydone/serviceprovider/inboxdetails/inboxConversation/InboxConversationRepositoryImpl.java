package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxConversation;

import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class InboxConversationRepositoryImpl implements InboxConversationRepository {
    private AnyDoneService anyDoneService;

    public InboxConversationRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessages(String token,
                                                                             String inboxId,
                                                                             long from,
                                                                             long to,
                                                                             int pageSize) {
        return anyDoneService.getThreadMessages(token, inboxId, from, to,
                pageSize, AnydoneProto.ServiceContext.INBOX_CONTEXT_VALUE);
    }
}