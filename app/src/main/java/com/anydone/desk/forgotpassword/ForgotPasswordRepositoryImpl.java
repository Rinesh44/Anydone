package com.anydone.desk.forgotpassword;

import androidx.annotation.NonNull;

import com.anydone.desk.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public class ForgotPasswordRepositoryImpl implements ForgotPasswordRepository {

    private AnyDoneService service;

    public ForgotPasswordRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> forgotPassword(@NonNull String emailPhone) {
        return service.forgotPassword(emailPhone);
    }
}
