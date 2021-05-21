package com.anydone.desk.account;

import com.anydone.desk.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.AuthRpcProto;

import io.reactivex.Observable;

public class AccountRepositoryImpl implements AccountRepository {
    AnyDoneService service;

    public AccountRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<AuthRpcProto.AuthBaseResponse> logout(String token) {
        return service.logout(token);
    }
}
