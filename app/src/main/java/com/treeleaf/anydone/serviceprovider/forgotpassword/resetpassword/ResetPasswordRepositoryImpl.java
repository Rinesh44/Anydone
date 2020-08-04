package com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import io.reactivex.Observable;

public class ResetPasswordRepositoryImpl implements ResetPasswordRepository {
    AnyDoneService service;
    private static final String TAG = "ResetPasswordRepository";

    public ResetPasswordRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> resetPassword(@NonNull String emailPhone,
                                                                   @NonNull String newPassword,
                                                                   @NonNull String confirmPassword,
                                                                   @NonNull String accountId,
                                                                   @NonNull String code) {

        UserProto.PasswordReset passwordReset = UserProto.PasswordReset.newBuilder()
                .setAccountId(accountId)
                .setCode(Integer.parseInt(code))
                .setEmailPhone(emailPhone)
                .setNewPassword(confirmPassword)
                .build();

        GlobalUtils.showLog(TAG, "Reset password check: " + passwordReset);

        return service.resetPassword(passwordReset);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> resendCode(@NonNull String emailPhone) {
        return service.resendCode(emailPhone);
    }
}
