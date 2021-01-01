package com.treeleaf.anydone.serviceprovider.ownedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface OwnedTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getOwnedTickets(String token,
                                                                         String serviceId,
                                                                         long from,
                                                                         long to,
                                                                         int page,
                                                                         String order);
}
