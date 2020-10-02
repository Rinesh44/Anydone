package com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline;

import com.treeleaf.anydone.entities.ConversationProto;
import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class ThreadTimelineRepositoryImpl implements ThreadTimelineRepository {
    private AnyDoneService anyDoneService;

    public ThreadTimelineRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<ConversationRpcProto.ConversationBaseResponse> getThreadById(String token, String threadId) {
        return anyDoneService.getConversationThreadById(token, threadId);
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> enableBot(String token,
                                                                           String threadId) {
        return anyDoneService.enableThreadBotReply(token, threadId);
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> disableBot(String token,
                                                                            String threadId) {
        return anyDoneService.disableThreadBotReply(token, threadId);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findEmployees(String token) {
        return anyDoneService.findEmployees(token);
    }

    @Override
    public Observable<ConversationRpcProto.ConversationBaseResponse> assignEmployeeToThread(String token,
                                                                                            ConversationProto.ConversationThread conversationThread) {
        return anyDoneService.assignEmployeeToThread(token, conversationThread);
    }

}
