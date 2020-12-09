package com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface UnsubscribedTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getSubscribeableTickets(String token,
                                                                                 String serviceId,
                                                                                 long from,
                                                                                 long to,
                                                                                 int page,
                                                                                 String sortOrder);

    Observable<TicketServiceRpcProto.TicketBaseResponse> subscribe(String token,
                                                                   long ticketId);
}
