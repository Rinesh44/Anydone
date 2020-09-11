package com.treeleaf.anydone.serviceprovider.threaddetails.threadconversation;

import com.treeleaf.anydone.entities.BotConversationProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.rpc.BotConversationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class ThreadConversationRepositoryImpl implements ThreadConversationRepository {
    private AnyDoneService anyDoneService;

    public ThreadConversationRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessages(String token,
                                                                             String threadId,
                                                                             long from,
                                                                             long to,
                                                                             int pageSize) {
        return anyDoneService.getThreadMessages(token, threadId, from, to,
                pageSize, RtcProto.RtcMessageContext.CONVERSATION_CONTEXT_VALUE);
    }

    @Override
    public Observable<BotConversationRpcProto.BotConversationBaseResponse>
    getSuggestions(String token, BotConversationProto.ConversationRequest request) {
        return anyDoneService.getTicketSuggestions(token, request);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> startTask(String token, long ticketId) {
        return anyDoneService.startTicket(token, String.valueOf(ticketId));
    }
}
