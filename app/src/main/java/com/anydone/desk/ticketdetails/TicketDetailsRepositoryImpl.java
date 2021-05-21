package com.anydone.desk.ticketdetails;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.GlobalUtils;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public class TicketDetailsRepositoryImpl implements TicketDetailsRepository {

    AnyDoneService anyDoneService;

    public TicketDetailsRepositoryImpl(AnyDoneService anyDoneService1) {
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        anyDoneService = retrofit.create(AnyDoneService.class);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getShareLink(String token,
                                                                             TicketProto.GetSharableLinkRequest
                                                                                     getSharableLinkRequest) {
        return anyDoneService.getLink(token, getSharableLinkRequest);
    }
}
