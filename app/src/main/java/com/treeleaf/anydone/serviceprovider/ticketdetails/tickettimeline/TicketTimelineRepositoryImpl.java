package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
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

}
