package com.anydone.desk.assignedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AssignedTicketRepositoryImpl implements AssignedTicketRepository {

    private AnyDoneService anyDoneService;

    public AssignedTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token, String serviceId, long from, long to, int page, String order) {
        return anyDoneService.getAssignedTickets(token, serviceId, page);
    }
}
