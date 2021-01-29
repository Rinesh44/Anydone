package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxtimeline;

import com.treeleaf.anydone.rpc.InboxRpcProto;

import java.util.List;

import io.reactivex.Observable;

public interface InboxTimelineRepository {
    Observable<InboxRpcProto.InboxBaseResponse> getInboxById(String token,
                                                             String inboxId);

    Observable<InboxRpcProto.InboxBaseResponse> addParticipants(String token,
                                                                String inboxId,
                                                                List<String> participantIds);
}
