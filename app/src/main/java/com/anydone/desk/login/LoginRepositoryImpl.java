package com.anydone.desk.login;


import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.rpc.AuthRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class LoginRepositoryImpl implements LoginRepository {
    AnyDoneService service;

    public LoginRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<AuthRpcProto.AuthBaseResponse> loginWithEmail(String email, String password) {
        AuthProto.LoginRequest loginRequest = AuthProto.LoginRequest.newBuilder()
                .setEmailPhone(email)
                .setPassword(password)
                .build();

        return service.login(loginRequest);
    }

    @Override
    public Observable<AuthRpcProto.AuthBaseResponse> loginWithPhone(String phone, String password) {
        AuthProto.LoginRequest loginRequest = AuthProto.LoginRequest.newBuilder()
                .setEmailPhone(phone)
                .setPassword(password)
                .build();

        return service.login(loginRequest);
    }
}
