package com.treeleaf.anydone.serviceprovider.linkshare;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface LinkShareRepository {
    Observable<TicketServiceRpcProto.TicketBaseResponse> getShareLink(String token,
                                                                      TicketProto.GetSharableLinkRequest
                                                                              sharableLinkRequest);
}
