package com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface ThreadTimelineRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketTimeline(String token,
                                                                           long ticketId);

    Observable<UserRpcProto.UserBaseResponse> findEmployees(String token);


}
