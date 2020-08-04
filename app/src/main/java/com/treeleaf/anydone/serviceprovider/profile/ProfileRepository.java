package com.treeleaf.anydone.serviceprovider.profile;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface ProfileRepository {

    Observable<UserRpcProto.UserBaseResponse> resendCode(String emailPhone);

    Observable<UserRpcProto.UserBaseResponse> addPhone(@NonNull String phone);

    Observable<UserRpcProto.UserBaseResponse> addEmail(@NonNull String email);
}
