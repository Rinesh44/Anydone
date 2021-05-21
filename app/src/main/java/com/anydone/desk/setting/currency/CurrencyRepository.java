package com.anydone.desk.setting.currency;


import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface CurrencyRepository {
    Observable<UserRpcProto.UserBaseResponse> addCurrency(String token, String currency);
}
