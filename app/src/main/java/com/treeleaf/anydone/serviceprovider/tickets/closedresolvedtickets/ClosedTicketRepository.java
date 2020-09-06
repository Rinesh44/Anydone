package com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface ClosedTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getClosedResolvedTickets(String token,
                                                                                  String serviceId,
                                                                                  long from,
                                                                                  long to,
                                                                                  int page);

    Observable<TicketServiceRpcProto.TicketBaseResponse> reopenTicket(String token,
                                                                      long ticketId);
}
