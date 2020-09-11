package com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class UnassignedTicketRepositoryImpl implements UnassignedTicketRepository {
    private AnyDoneService anyDoneService;

    public UnassignedTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignableTickets(String token,
                                                                                     String serviceId,
                                                                                     long from,
                                                                                     long to,
                                                                                     int page) {
        return anyDoneService.getAssignableTickets(token, serviceId, from, to, page);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> assignTicket(String token,
                                                                             long ticketId,
                                                                             TicketProto.Ticket assignedEmployee) {
        return anyDoneService.assignEmployee(token, ticketId, assignedEmployee);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findEmployees(String token) {
        return anyDoneService.findEmployees(token);
    }
}
