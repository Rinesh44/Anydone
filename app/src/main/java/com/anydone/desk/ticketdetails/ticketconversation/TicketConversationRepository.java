package com.anydone.desk.ticketdetails.ticketconversation;

import com.treeleaf.anydone.entities.BotConversationProto;
import com.treeleaf.anydone.rpc.BotConversationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface TicketConversationRepository {
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessages(String token, long ticketId,
                                                                      long from,
                                                                      long to,
                                                                      int pageSize);

    Observable<BotConversationRpcProto.BotConversationBaseResponse>
    getSuggestions(String token, BotConversationProto.ConversationRequest request);

    Observable<TicketServiceRpcProto.TicketBaseResponse>
    startTask(String token, long ticketId);

}
