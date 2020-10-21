package com.treeleaf.anydone.serviceprovider.tickets.inprogresstickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface InProgressTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getSubscribedTickets(String token,
                                                                              String serviceId,
                                                                              long from,
                                                                              long to,
                                                                              int page);

    Observable<TicketServiceRpcProto.TicketBaseResponse> unsubscribe(String token, long ticketId);
}
