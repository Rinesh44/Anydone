package com.anydone.desk.tickets.closedresolvedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface ClosedTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getClosedResolvedTickets(String token,
                                                                                  String serviceId,
                                                                                  long from,
                                                                                  long to,
                                                                                  int page,
                                                                                  String sortOrder);

    Observable<TicketServiceRpcProto.TicketBaseResponse> reopenTicket(String token,
                                                                      long ticketId);
}
