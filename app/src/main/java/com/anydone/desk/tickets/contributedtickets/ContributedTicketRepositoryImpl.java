package com.anydone.desk.tickets.contributedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class ContributedTicketRepositoryImpl implements ContributedTicketRepository {
    private AnyDoneService anyDoneService;

    public ContributedTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse>
    getContributedTickets(String token, String serviceId, long from, long to, int page,
                          String sortOrder) {
        return anyDoneService.getContributedTickets(token, serviceId, from, to, page, sortOrder);
    }
}
