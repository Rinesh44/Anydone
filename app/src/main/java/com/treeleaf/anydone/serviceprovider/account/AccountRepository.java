package com.treeleaf.anydone.serviceprovider.account;

import com.treeleaf.anydone.rpc.AuthRpcProto;

import io.reactivex.Observable;

public interface AccountRepository {
    Observable<AuthRpcProto.AuthBaseResponse> logout(String token);
}
