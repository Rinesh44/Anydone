package com.anydone.desk.forgotpassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface ForgotPasswordRepository {

    Observable<UserRpcProto.UserBaseResponse> forgotPassword(@NonNull String emailPhone);

}