package com.treeleaf.anydone.serviceprovider.servicerequestdetail;

import com.treeleaf.anydone.entities.BotConversationProto;
import com.treeleaf.anydone.rpc.BotConversationRpcProto;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;

import io.reactivex.Observable;

public interface ServiceRequestDetailRepository {
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getServiceDoers(String token,
                                                                              long orderId);


    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessages(String token, long refId,
                                                                      long from,
                                                                      long to,
                                                                      int pageSize);

    Observable<BotConversationRpcProto.BotConversationBaseResponse>
    getSuggestions(String token, BotConversationProto.ConversationRequest request);

}
