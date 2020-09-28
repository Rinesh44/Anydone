package com.treeleaf.anydone.serviceprovider.addcontributor;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AddContributorRepositoryImpl implements AddContributorRepository {
    private AnyDoneService anyDoneService;

    public AddContributorRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse>
    addContributor(String token, long ticketId,
                   TicketProto.Ticket employees) {
        return anyDoneService.addContributors(token, String.valueOf(ticketId), employees);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findContributors(String token) {
        return anyDoneService.findEmployees(token);
    }
}
