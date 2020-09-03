package com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface UnassignedTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignableTickets(String token,
                                                                              long from,
                                                                              long to,
                                                                              int page);

    Observable<TicketServiceRpcProto.TicketBaseResponse> assignTicket(String token,
                                                                      long ticketId,
                                                                      TicketProto.Ticket assignedEmployee);

    Observable<UserRpcProto.UserBaseResponse> findEmployees(String token);
}
