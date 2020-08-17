package com.treeleaf.anydone.serviceprovider.tickets.assignedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface AssignedTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token,
                                                                            long from,
                                                                            long to,
                                                                            int page);
}
