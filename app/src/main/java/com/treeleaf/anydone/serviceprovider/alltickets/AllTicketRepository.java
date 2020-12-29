package com.treeleaf.anydone.serviceprovider.alltickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface AllTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getAllTickets(String token,
                                                                       String serviceId,
                                                                       long from,
                                                                       long to,
                                                                       int page,
                                                                       String sortOrder);
}
