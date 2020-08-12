package com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface ResetPasswordRepository {
    Observable<UserRpcProto.UserBaseResponse> resetPassword(@NonNull String emailPhone,
                                                            @NonNull String newPassword,
                                                            @NonNull String confirmPassword,
                                                            @NonNull String accountId,
                                                            @NonNull String code);

    Observable<UserRpcProto.UserBaseResponse> resendCode(@NonNull String emailPhone);

    Observable<UserRpcProto.UserBaseResponse> changePassword(@NonNull String token,
                                                             @NonNull String oldPassword,
                                                             @NonNull String NewPassword);
}
