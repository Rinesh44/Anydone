package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface TicketsRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token,
                                                                            String serviceId,
                                                                            long from,
                                                                            long to,
                                                                            int page);

    Observable<OrderServiceRpcProto.OrderServiceBaseResponse>
    filterServiceRequests(String token,
                          String serviceName,
                          long from,
                          long to,
                          String status);

    Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token);
}

