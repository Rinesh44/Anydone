package com.treeleaf.anydone.serviceprovider.ticketdetails;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class TicketDetailsRepositoryImpl implements TicketDetailsRepository {
    private AnyDoneService anyDoneService;

    public TicketDetailsRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getShareLink(String token,
                                                                             TicketProto.GetSharableLinkRequest
                                                                                     getSharableLinkRequest) {
        return anyDoneService.getLink(token, getSharableLinkRequest);
    }
}
