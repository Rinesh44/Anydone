package com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface VerifyCodeRepository {
    Observable<UserRpcProto.UserBaseResponse> resendCode(@NonNull String emailPhone);
}