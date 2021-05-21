package com.anydone.desk.tickets.unsubscribedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class UnsubscribedTicketRepositoryImpl implements UnsubscribedTicketRepository {
    private AnyDoneService anyDoneService;

    public UnsubscribedTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getSubscribeableTickets(String token,
                                                                                        String serviceId,
                                                                                        long from,
                                                                                        long to,
                                                                                        int page,
                                                                                        String sortOrder) {
        return anyDoneService.getSubscribeableTickets(token, serviceId, from, to, page, sortOrder);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> subscribe(String token, long ticketId) {
        return anyDoneService.subscribe(token, ticketId);
    }
}
