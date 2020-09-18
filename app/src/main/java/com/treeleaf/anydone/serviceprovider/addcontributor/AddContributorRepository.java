package com.treeleaf.anydone.serviceprovider.addcontributor;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface AddContributorRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> addContributor(String token, long ticketId,
                                                                        TicketProto.Ticket contributors);

    Observable<UserRpcProto.UserBaseResponse> findContributors(String token);

}