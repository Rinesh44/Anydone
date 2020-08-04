package com.treeleaf.anydone.serviceprovider.changepassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface ChangePasswordRepository {
    Observable<UserRpcProto.UserBaseResponse> changePassword(@NonNull String token,
                                                             @NonNull String oldPassword,
                                                             @NonNull String NewPassword);
}
