package com.anydone.desk.ticketdetails;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface TicketDetailsRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getShareLink(String token,
                                                                      TicketProto.GetSharableLinkRequest
                                                                              sharableLinkRequest);
}
