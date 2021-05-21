package com.anydone.desk.alltickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AllTicketsRepositoryImpl implements AllTicketRepository {
    private AnyDoneService anyDoneService;

    public AllTicketsRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse>
    getAllTickets(String token, String serviceId, long from, long to, int page,
                  String sortOrder) {
        return anyDoneService.getDependencyTickets(token, serviceId);
    }
}
