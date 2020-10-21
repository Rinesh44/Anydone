package com.treeleaf.anydone.serviceprovider.tickets.pendingtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class PendingTicketRepositoryImpl implements PendingTicketRepository {
    private AnyDoneService anyDoneService;

    public PendingTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token, String serviceId,
                                                                                   long from, long to, int page) {
        return anyDoneService.getAssignedTickets(token, serviceId, from, to, page);
    }
}
