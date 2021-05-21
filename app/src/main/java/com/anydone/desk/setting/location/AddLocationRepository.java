package com.anydone.desk.setting.location;

import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface AddLocationRepository {
    Observable<UserRpcProto.UserBaseResponse> addLocation(String token, UserProto.Location location);
}
