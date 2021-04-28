package com.treeleaf.anydone.serviceprovider.tickets;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;

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
                                                                                   int page,
                                                                                   String sortOrder) {
        return service.getAssignedTickets(token, serviceId, page);
    }

/*    @Override
    public Observable<OrderServiceRpcProto.OrderServiceBaseResponse> filterServiceRequests(
            String token, String serviceName, long from, long to, String status) {
        return service.filterServiceRequests(token, serviceName, from, to, status);
    }*/

    @Override
    public Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token) {
        return service.getServices(token);
    }


/*    @Override
    public Observable<UserRpcProto.UserBaseResponse> findConsumers(String token) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        return service.findCustomers(token, serviceId, 0, System.currentTimeMillis(),
                100);
    }*/

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findEmployees(String token) {
        return service.findEmployees(token);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> findTags(String token) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        return service.getTicketTeams(token, serviceId);
    }
}
