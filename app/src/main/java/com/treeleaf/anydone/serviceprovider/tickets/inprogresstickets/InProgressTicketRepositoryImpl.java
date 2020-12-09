package com.treeleaf.anydone.serviceprovider.tickets.inprogresstickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class InProgressTicketRepositoryImpl implements InProgressTicketRepository {
    private AnyDoneService anyDoneService;

    public InProgressTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getSubscribedTickets(String token,
                                                                                     String serviceId,
                                                                                     long from,
                                                                                     long to,
                                                                                     int page,
                                                                                     String sortOrder) {
        return anyDoneService.getSubscribedTickets(token, serviceId, from, to, page, sortOrder);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> unsubscribe(String token, long ticketId) {
        return anyDoneService.unsubscribe(token, ticketId);
    }
}
