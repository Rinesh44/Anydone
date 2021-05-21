package com.anydone.desk.tickets.pendingtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class PendingTicketRepositoryImpl implements PendingTicketRepository {
    private AnyDoneService anyDoneService;

    public PendingTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token, String serviceId,
                                                                                   long from, long to, int page, String sortOrder) {
        return anyDoneService.getAssignedTickets(token, serviceId, page);
    }
}
