package com.treeleaf.anydone.serviceprovider.setting.currency;


import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface CurrencyRepository {
    Observable<UserRpcProto.UserBaseResponse> addCurrency(String token, String currency);
}
