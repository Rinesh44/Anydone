package com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class ThreadTimelineRepositoryImpl implements ThreadTimelineRepository {
    private AnyDoneService anyDoneService;

    public ThreadTimelineRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketTimeline(String token,
                                                                                  long ticketId) {
        return anyDoneService.getTicketTimeline(token, ticketId);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findEmployees(String token) {
        return anyDoneService.findEmployees(token);
    }
}
