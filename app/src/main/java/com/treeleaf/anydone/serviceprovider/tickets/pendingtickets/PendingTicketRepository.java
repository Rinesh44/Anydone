package com.treeleaf.anydone.serviceprovider.tickets.pendingtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface PendingTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token,
                                                                            String serviceId,
                                                                            long from,
                                                                            long to,
                                                                            int page);
}
