package com.anydone.desk.opentickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class OpenTicketRepositoryImpl implements OpenTicketRepository {
    private AnyDoneService anyDoneService;

    public OpenTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getOpenTickets(String token,
                                                                               String serviceId,
                                                                               long from,
                                                                               long to,
                                                                               int page,
                                                                               String order) {
        return anyDoneService.getBacklogTickets(token, serviceId, from, to, page, order);
    }
}
