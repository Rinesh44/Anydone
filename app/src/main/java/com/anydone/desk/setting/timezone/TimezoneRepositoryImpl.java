package com.anydone.desk.setting.timezone;

import com.anydone.desk.rest.service.AnyDoneService;
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
