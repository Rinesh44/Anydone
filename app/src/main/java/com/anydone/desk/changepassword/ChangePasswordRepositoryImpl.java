package com.anydone.desk.changepassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.entities.UserProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public class ChangePasswordRepositoryImpl implements ChangePasswordRepository {
    AnyDoneService service;

    public ChangePasswordRepositoryImpl(AnyDoneService service) {
        this.service = service;
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
