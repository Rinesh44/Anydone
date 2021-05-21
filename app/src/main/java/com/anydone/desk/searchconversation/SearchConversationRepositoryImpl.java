package com.anydone.desk.searchconversation;

import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class SearchConversationRepositoryImpl implements SearchConversationRepository {
    AnyDoneService service;
    private static final String TAG = "SearchConversationRepos";

    public SearchConversationRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> searchMessages(String query) {
        return null;
    }
}
