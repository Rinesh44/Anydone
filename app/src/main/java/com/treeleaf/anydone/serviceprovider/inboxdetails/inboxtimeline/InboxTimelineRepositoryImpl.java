package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxtimeline;

import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import java.util.List;

import io.reactivex.Observable;

public class InboxTimelineRepositoryImpl implements InboxTimelineRepository {
    private AnyDoneService anyDoneService;

    public InboxTimelineRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<InboxRpcProto.InboxBaseResponse> getInboxById(String token, String inboxId) {
        return null;
    }

    @Override
    public Observable<InboxRpcProto.InboxBaseResponse> addParticipants(String token,
                                                                       String inboxId, List<String> participantIds) {
        return null;
    }
}
