package com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class UnsubscribedTicketRepositoryImpl implements UnsubscribedTicketRepository {
    private AnyDoneService anyDoneService;

    public UnsubscribedTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getSubscribeableTickets(String token,
                                                                                        long from,
                                                                                        long to,
                                                                                        int page) {
        return anyDoneService.getSubscribeableTickets(token, from, to, page);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> subscribe(String token, long ticketId) {
        return anyDoneService.subscribe(token, ticketId);
    }
}