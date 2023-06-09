package com.anydone.desk.threaddetails.threadconversation;

import com.treeleaf.anydone.entities.BotConversationProto;
import com.treeleaf.anydone.rpc.BotConversationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface ThreadConversationRepository {
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessages(String token, String threadId,
                                                                      long from,
                                                                      long to,
                                                                      int pageSize);

    Observable<BotConversationRpcProto.BotConversationBaseResponse>
    getSuggestions(String token, BotConversationProto.ConversationRequest request);

    Observable<TicketServiceRpcProto.TicketBaseResponse>
    startTask(String token, long ticketId);

}
