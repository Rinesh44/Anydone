package com.treeleaf.anydone.serviceprovider.tickets.assignedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AssignedTicketRepositoryImpl implements AssignedTicketRepository {
    private AnyDoneService anyDoneService;

    public AssignedTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token, long from, long to, int page) {
        return anyDoneService.getAssignedTickets(token, from, to, page);
    }
}
