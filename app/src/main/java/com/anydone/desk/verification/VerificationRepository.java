package com.anydone.desk.verification;

import com.treeleaf.anydone.rpc.AuthRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface VerificationRepository {

    Observable<UserRpcProto.UserBaseResponse> verifyDigitsWithPhone(String digits);

    Observable<UserRpcProto.UserBaseResponse> verifyDigitsWithEmail(String digits);

    Observable<UserRpcProto.UserBaseResponse> resendCode(String emailPhone);

    Observable<AuthRpcProto.AuthBaseResponse> login(String emailPhone, String password);

}
