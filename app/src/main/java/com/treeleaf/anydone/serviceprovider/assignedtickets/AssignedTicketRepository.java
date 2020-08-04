package com.treeleaf.anydone.serviceprovider.assignedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface AssignedTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getTickets(String token);
}
