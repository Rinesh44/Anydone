package com.anydone.desk.forgotpassword.verifyCode;

import androidx.annotation.NonNull;

import com.anydone.desk.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public class VerifyCodeRepositoryImpl implements VerifyCodeRepository {
    private AnyDoneService service;

    public VerifyCodeRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }


    @Override
    public Observable<UserRpcProto.UserBaseResponse> resendCode(@NonNull String emailPhone) {
        return service.resendCode(emailPhone);
    }
}
