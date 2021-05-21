package com.anydone.desk.reply;

import com.treeleaf.anydone.rpc.InboxRpcProto;

import io.reactivex.Observable;

public interface ReplyRepository {
    Observable<InboxRpcProto.InboxBaseResponse> getReplies(String msgId);
}
