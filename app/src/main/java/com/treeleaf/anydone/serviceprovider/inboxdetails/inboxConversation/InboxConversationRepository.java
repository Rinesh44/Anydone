package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxConversation;

import com.treeleaf.anydone.rpc.RtcServiceRpcProto;

import io.reactivex.Observable;

public interface InboxConversationRepository {
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessages(String token, String inboxId,
                                                                      long from,
                                                                      long to,
                                                                      int pageSize);

}
