package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class TicketTimelineRepositoryImpl implements TicketTimelineRepository {
    private AnyDoneService anyDoneService;

    public TicketTimelineRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketTimeline(String token,
                                                                                  long ticketId) {
        return anyDoneService.getTicketTimeline(token, ticketId);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse>
    unAssignContributor(String token,
                        String ticketId,
                        TicketProto.TicketContributor ticketContributor) {
        return anyDoneService.deleteContributor(token, ticketId, ticketContributor);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> resolveTicket(String token, long ticketId) {
        return anyDoneService.resolveTicket(token, ticketId);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> reopenTicket(String token, long ticketId,
                                                                             String remark) {
        return anyDoneService.reopenTicket(token, ticketId, remark);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> closeTicket(String token, long ticketId,
                                                                            String remark) {
        return anyDoneService.closeTicket(token, ticketId, remark);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findEmployees(String token) {
        return anyDoneService.findEmployees(token);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> assignTicket(String token,
                                                                             long ticketId,
                                                                             TicketProto.Ticket assignedEmployee) {
        return anyDoneService.assignEmployee(token, ticketId, assignedEmployee);
    }

}
