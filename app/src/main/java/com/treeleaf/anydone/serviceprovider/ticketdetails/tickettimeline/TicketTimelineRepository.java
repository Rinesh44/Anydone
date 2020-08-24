package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;
import retrofit2.http.HEAD;

public interface TicketTimelineRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketTimeline(String token,
                                                                           long ticketId);



}
