package com.treeleaf.anydone.serviceprovider.tickets.subscribetickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class SubscribeTicketRepositoryImpl implements SubscribeTicketRepository {
    private AnyDoneService anyDoneService;

    public SubscribeTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getSubscribedTickets(String token,
                                                                                     String serviceId,
                                                                                     long from,
                                                                                     long to,
                                                                                     int page) {
        return anyDoneService.getSubscribedTickets(token, serviceId, from, to, page);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> unsubscribe(String token, long ticketId) {
        return anyDoneService.unsubscribe(token, ticketId);
    }
}
