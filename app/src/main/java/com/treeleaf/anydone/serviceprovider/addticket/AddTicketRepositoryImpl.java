package com.treeleaf.anydone.serviceprovider.addticket;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AddTicketRepositoryImpl implements AddTicketRepository {
    private AnyDoneService anyDoneService;

    public AddTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> createTicket(String token, TicketProto.Ticket ticket) {
        return anyDoneService.createTicket(token, ticket);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findConsumers(String token) {
        return anyDoneService.findConsumers(token);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findEmployees(String token) {
        return anyDoneService.findEmployees(token);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> findTags(String token) {
        return anyDoneService.findTag(token);
    }
}
