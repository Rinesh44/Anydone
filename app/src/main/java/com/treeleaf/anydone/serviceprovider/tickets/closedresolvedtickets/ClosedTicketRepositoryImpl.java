package com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class ClosedTicketRepositoryImpl implements ClosedTicketRepository {
    private AnyDoneService anyDoneService;

    public ClosedTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getClosedResolvedTickets(String token,
                                                                                         String serviceId,
                                                                                         long from,
                                                                                         long to,
                                                                                         int page) {
        return anyDoneService.getClosedResolvedTickets(token, serviceId, from, to, page);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> reopenTicket(String token, long ticketId) {
        return anyDoneService.reopenTicket(token, ticketId, "reopen ticket");
    }
}
