package com.treeleaf.anydone.serviceprovider.setting.timezone;


import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface TimezoneRepository {
    Observable<UserRpcProto.UserBaseResponse> addTimezone(String token, String timezone);
}
