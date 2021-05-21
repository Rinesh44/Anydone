package com.anydone.desk.assignedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface AssignedTicketRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token,
                                                                        String serviceId,
                                                                        long from,
                                                                        long to,
                                                                        int page,
                                                                        String order);
}
