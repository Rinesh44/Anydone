package com.anydone.desk.customertickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface CustomerTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getCustomerTickets(String token,
                                                                            String serviceId,
                                                                            String customerId,
                                                                            long from,
                                                                            long to,
                                                                            int page);
}
