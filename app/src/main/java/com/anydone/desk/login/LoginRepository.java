package com.anydone.desk.login;


import com.treeleaf.anydone.rpc.AuthRpcProto;

import io.reactivex.Observable;

public interface LoginRepository {

    Observable<AuthRpcProto.AuthBaseResponse> loginWithEmail(String email, String password);

    Observable<AuthRpcProto.AuthBaseResponse> loginWithPhone(String phone, String password);

}
