package com.treeleaf.anydone.serviceprovider.forgotpassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
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
