package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class TicketsRepositoryImpl implements TicketsRepository {
    AnyDoneService service;

    public TicketsRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(String token,
                                                                                   String serviceId,
                                                                                   long from,
                                                                                   long to,
                                                                                   int page) {
        return service.getAssignedTickets(token, serviceId, from, to, page);
    }

    @Override
    public Observable<OrderServiceRpcProto.OrderServiceBaseResponse> filterServiceRequests(
            String token, String serviceName, long from, long to, String status) {
        return service.filterServiceRequests(token, serviceName, from, to, status);
    }

    @Override
    public Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token) {
        return service.getServices(token);
    }


    @Override
    public Observable<UserRpcProto.UserBaseResponse> findConsumers(String token) {
        return service.findConsumers(token);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findEmployees(String token) {
        return service.findEmployees(token);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> findTags(String token) {
        return service.findTag(token);
    }
}
