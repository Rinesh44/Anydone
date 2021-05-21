package com.anydone.desk.addparticipant;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AddParticipantRepositoryImpl implements AddParticipantRepository {
    AnyDoneService anyDoneService;

    public AddParticipantRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> addContributor(String token,
                                                                               long ticketId,
                                                                               TicketProto.Ticket contributors) {
        return null;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> findContributors(String token) {
        return null;
    }
}
