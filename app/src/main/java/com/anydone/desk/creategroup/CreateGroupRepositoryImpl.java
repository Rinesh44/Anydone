package com.anydone.desk.creategroup;

import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import java.util.List;

import io.reactivex.Observable;

public class CreateGroupRepositoryImpl implements CreateGroupRepository{
    AnyDoneService anyDoneService;

    public CreateGroupRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<InboxRpcProto.InboxBaseResponse> createGroup(List<String> participants,
                                                                   String msg, String subject) {
        return null;
    }
}
