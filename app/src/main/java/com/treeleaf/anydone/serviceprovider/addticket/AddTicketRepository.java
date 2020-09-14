package com.treeleaf.anydone.serviceprovider.addticket;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface AddTicketRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> createTicket(String token,
                                                                      TicketProto.Ticket ticket);

}
