package com.anydone.desk.forgotpassword.resetpassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.entities.UserProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.utils.GlobalUtils;

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

    @Override
    public Observable<UserRpcProto.UserBaseResponse> changePassword(@NonNull String token,
                                                                    @NonNull String oldPassword,
                                                                    @NonNull String newPassword) {
        UserProto.PasswordChangeRequest passwordChangeRequest =
                UserProto.PasswordChangeRequest.newBuilder()
                        .setOldPassword(oldPassword)
                        .setNewPassword(newPassword)
                        .build();

        return service.changePassword(token, passwordChangeRequest);
    }
}
