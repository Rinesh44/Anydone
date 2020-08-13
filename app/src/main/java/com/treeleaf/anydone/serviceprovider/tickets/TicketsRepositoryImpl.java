package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class TicketsRepositoryImpl implements TicketsRepository {
    AnyDoneService service;

    public TicketsRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token,
                                                                                   long from,
                                                                                   long to,
                                                                                   int page) {
        return service.getAssignedTickets(token, from, to, page);
    }

    @Override
    public Observable<OrderServiceRpcProto.OrderServiceBaseResponse> filterServiceRequests(
            String token, String serviceName, long from, long to, String status) {
        return null;
    }

}
