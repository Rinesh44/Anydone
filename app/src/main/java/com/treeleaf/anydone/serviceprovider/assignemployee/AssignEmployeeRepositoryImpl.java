package com.treeleaf.anydone.serviceprovider.assignemployee;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AssignEmployeeRepositoryImpl implements AssignEmployeeRepository {
    private AnyDoneService anyDoneService;

    public AssignEmployeeRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> assignEmployee(String token, long ticketId,
                                                                               TicketProto.Ticket employees) {
        return anyDoneService.assignEmployee(token, ticketId, employees);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findEmployees(String token) {
        return anyDoneService.findEmployees(token);
    }
}
