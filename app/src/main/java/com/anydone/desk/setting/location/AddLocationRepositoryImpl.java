package com.anydone.desk.setting.location;

import com.treeleaf.anydone.entities.UserProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public class AddLocationRepositoryImpl implements AddLocationRepository {
    AnyDoneService service;

    public AddLocationRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> addLocation(String token,
                                                                 UserProto.Location location) {
        return service.addLocation(token, location);
    }
}
