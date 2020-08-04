package com.treeleaf.anydone.serviceprovider.setting.timezone;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;


public class TimezoneRepositoryImpl implements TimezoneRepository {
    AnyDoneService anyDoneService;

    public TimezoneRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> addTimezone(String token,
                                                                 String timezone) {
        return anyDoneService.addTimezone(token, timezone);
    }
}
