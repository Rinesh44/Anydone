package com.anydone.desk.tickets.contributedtickets;

import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface ContributedTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getContributedTickets(String token,
                                                                               String serviceId,
                                                                               long from,
                                                                               long to,
                                                                               int page,
                                                                               String sortOrder);
}
