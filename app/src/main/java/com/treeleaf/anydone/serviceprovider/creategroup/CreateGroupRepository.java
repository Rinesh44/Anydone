package com.treeleaf.anydone.serviceprovider.creategroup;

import com.treeleaf.anydone.rpc.InboxRpcProto;

import java.util.List;

import io.reactivex.Observable;

public interface CreateGroupRepository {
    Observable<InboxRpcProto.InboxBaseResponse> createGroup(List<String> participants, String
            msg, String subject);
}
