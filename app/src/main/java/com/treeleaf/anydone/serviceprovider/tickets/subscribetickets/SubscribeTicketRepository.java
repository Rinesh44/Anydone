package com.treeleaf.anydone.serviceprovider.tickets.subscribetickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface SubscribeTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getSubscribedTickets(String token,
                                                                              long from,
                                                                              long to,
                                                                              int page);

    Observable<TicketServiceRpcProto.TicketBaseResponse> unsubscribe(String token, long ticketId);
}
