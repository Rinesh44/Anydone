package com.treeleaf.anydone.serviceprovider.ownedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class OwnedTicketRepositoryImpl implements OwnedTicketRepository {
    private AnyDoneService anyDoneService;

    public OwnedTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getOwnedTickets
            (String token, String serviceId, long from, long to, int page, String order) {
        return anyDoneService.getBacklogTickets(token, serviceId, from, to, page, order);
    }
}
