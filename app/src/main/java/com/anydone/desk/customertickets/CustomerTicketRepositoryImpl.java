package com.anydone.desk.customertickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class CustomerTicketRepositoryImpl implements CustomerTicketRepository {
    private AnyDoneService anyDoneService;

    public CustomerTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getCustomerTickets
            (String token, String serviceId, String customerId, long from, long to, int page) {

        return anyDoneService.getCustomerTickets(token, serviceId, customerId, from, to, page);
    }
}
