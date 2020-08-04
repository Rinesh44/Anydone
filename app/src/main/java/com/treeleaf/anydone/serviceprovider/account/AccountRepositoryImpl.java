package com.treeleaf.anydone.serviceprovider.account;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
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
