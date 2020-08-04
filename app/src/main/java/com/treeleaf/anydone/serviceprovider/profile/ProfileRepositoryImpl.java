package com.treeleaf.anydone.serviceprovider.profile;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.Constants;

import io.reactivex.Observable;

public class ProfileRepositoryImpl implements ProfileRepository {
    AnyDoneService service;
    private static final String TAG = "ProfileRepositoryImpl";

    public ProfileRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> resendCode(String emailPhone) {
        return service.resendCode(emailPhone.toLowerCase());
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> addPhone(@NonNull String phone) {
        return service.addPhone(Hawk.get(Constants.TOKEN), phone);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> addEmail(@NonNull String email) {
        return service.addEmail(Hawk.get(Constants.TOKEN), email.toLowerCase());
    }
}
