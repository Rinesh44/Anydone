package com.anydone.desk.forwardMessage;

import com.treeleaf.anydone.rpc.InboxRpcProto;

import java.util.List;

import io.reactivex.Observable;

public interface ForwardMessageRepository {
    Observable<InboxRpcProto.InboxBaseResponse> forwardMessage(List<String> participants, String
            msg);
}
