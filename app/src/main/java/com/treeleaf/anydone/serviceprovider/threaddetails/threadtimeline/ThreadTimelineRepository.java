package com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline;

import com.treeleaf.anydone.entities.ConversationProto;
import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface ThreadTimelineRepository {

    Observable<ConversationRpcProto.ConversationBaseResponse> getThreadById(String token,
                                                                            String threadId);

    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> enableBot(String token, String threadId);

    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> disableBot(String token, String threadId);

    Observable<UserRpcProto.UserBaseResponse> findEmployees(String token);

    Observable<ConversationRpcProto.ConversationBaseResponse> assignEmployeeToThread(String token,
                                                                                     ConversationProto.ConversationThread
                                                                                             conversationThread);

}

