package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface TicketsRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTickets(String token);

    Observable<OrderServiceRpcProto.OrderServiceBaseResponse>
    filterServiceRequests(String token,
                          String serviceName,
                          long from,
                          long to,
                          String status);
}

