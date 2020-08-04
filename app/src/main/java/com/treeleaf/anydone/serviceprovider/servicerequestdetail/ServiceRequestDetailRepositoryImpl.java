package com.treeleaf.anydone.serviceprovider.servicerequestdetail;

import com.treeleaf.anydone.entities.BotConversationProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.BotConversationRpcProto;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;

import io.reactivex.Observable;

public class ServiceRequestDetailRepositoryImpl implements ServiceRequestDetailRepository {

    AnyDoneService service;

    public ServiceRequestDetailRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getServiceDoers(String token,
                                                                                     long orderId) {
        return service.getServiceDoers(token, orderId);
    }

    @Override
    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessages(String token,
                                                                             long refId, long from,
                                                                             long to, int pageSize) {
        return service.getMessages(token, refId, from, to, pageSize);
    }

    @Override
    public Observable<BotConversationRpcProto.BotConversationBaseResponse>
    getSuggestions(String token, BotConversationProto.ConversationRequest request) {
        return service.getSuggestions(token, request);
    }
}
