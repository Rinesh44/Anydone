package com.treeleaf.anydone.serviceprovider.searchconversation;

import com.treeleaf.anydone.rpc.RtcServiceRpcProto;

import io.reactivex.Observable;

public interface SearchConversationRepository {

    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> searchMessages(String query);

}
