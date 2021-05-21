package com.anydone.desk.setting.currency;


import com.anydone.desk.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    private AnyDoneService anyDoneService;

    public CurrencyRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> addCurrency(String token, String currency) {
        return anyDoneService.addCurrency(token, currency);
    }
}
