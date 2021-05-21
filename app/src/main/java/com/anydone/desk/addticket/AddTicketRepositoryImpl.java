package com.anydone.desk.addticket;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AddTicketRepositoryImpl implements AddTicketRepository {
    private AnyDoneService anyDoneService;

    public AddTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse>
    createTicket(String token, TicketProto.Ticket ticket) {
        return anyDoneService.createTicket(token, ticket);
    }

}
