package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface TicketsRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token,
                                                                            String serviceId,
                                                                            long from,
                                                                            long to,
                                                                            int page,
                                                                            String sortOrder);

    Observable<OrderServiceRpcProto.OrderServiceBaseResponse>
    filterServiceRequests(String token,
                          String serviceName,
                          long from,
                          long to,
                          String status);

    Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token);

    Observable<UserRpcProto.UserBaseResponse> findConsumers(String token);

    Observable<UserRpcProto.UserBaseResponse> findEmployees(String token);

    Observable<TicketServiceRpcProto.TicketBaseResponse> findTags(String token);
}

