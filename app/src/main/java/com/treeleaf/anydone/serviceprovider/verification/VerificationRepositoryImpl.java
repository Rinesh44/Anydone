package com.treeleaf.anydone.serviceprovider.verification;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.AuthRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import io.reactivex.Observable;

public class VerificationRepositoryImpl implements VerificationRepository {
    private static final String TAG = "VerificationRepositoryI";
    AnyDoneService service;

    public VerificationRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> verifyDigitsWithPhone(String digits) {
        UserProto.UserVerification userVerification = UserProto.UserVerification.newBuilder()
                .setEmailPhone(Hawk.get(Constants.EMAIL_PHONE, ""))
                .setCode(Integer.parseInt(digits))
                .build();

        GlobalUtils.showLog(TAG, "Validation: " + Hawk.get(Constants.EMAIL_PHONE));
        GlobalUtils.showLog(TAG, "Validation: " + digits);

        return service.verifyCodeWithPhone(userVerification);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> verifyDigitsWithEmail(String digits) {
        UserProto.UserVerification userVerification = UserProto.UserVerification.newBuilder()
                .setEmailPhone(Hawk.get(Constants.EMAIL_PHONE, ""))
                .setCode(Integer.parseInt(digits))
                .build();

        GlobalUtils.showLog(TAG, "Validation: " + Hawk.get(Constants.EMAIL_PHONE));
        GlobalUtils.showLog(TAG, "Validation: " + digits);

        return service.verifyCodeWithEmail(userVerification);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> resendCode(String emailPhone) {
        return service.resendCode(emailPhone);
    }

    @Override
    public Observable<AuthRpcProto.AuthBaseResponse> login(String emailPhone, String password) {
        AuthProto.LoginRequest loginRequest = AuthProto.LoginRequest.newBuilder()
                .setEmailPhone(emailPhone)
                .setPassword(password)
                .build();

        return service.login(loginRequest);
    }

}
