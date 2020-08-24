package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation;

import com.treeleaf.anydone.entities.BotConversationProto;
import com.treeleaf.anydone.rpc.BotConversationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class TicketConversationRepositoryImpl implements TicketConversationRepository {
    private AnyDoneService anyDoneService;

    public TicketConversationRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessages(String token,
                                                                             long ticketId,
                                                                             long from,
                                                                             long to,
                                                                             int pageSize) {
        return anyDoneService.getTicketMessages(token, ticketId, to, from, pageSize);
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
