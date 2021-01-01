package com.treeleaf.anydone.serviceprovider.opentickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface OpenTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getOpenTickets(String token,
                                                                        String serviceId,
                                                                        long from,
                                                                        long to,
                                                                        int page,
                                                                        String order);
}
