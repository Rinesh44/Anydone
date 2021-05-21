package com.anydone.desk.changepassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface ChangePasswordRepository {
    Observable<UserRpcProto.UserBaseResponse> changePassword(@NonNull String token,
                                                             @NonNull String oldPassword,
                                                             @NonNull String NewPassword);
}
