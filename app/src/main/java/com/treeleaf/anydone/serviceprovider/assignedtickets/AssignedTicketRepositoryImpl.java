package com.treeleaf.anydone.serviceprovider.assignedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AssignedTicketRepositoryImpl implements AssignedTicketRepository {
    AnyDoneService anyDoneService;

    public AssignedTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getTickets(String token) {
        return anyDoneService.getAllTickets(token);
    }
}
