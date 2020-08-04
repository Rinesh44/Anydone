package com.treeleaf.anydone.serviceprovider.setting.location.showLocation;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class ShowLocationRepositoryImpl implements ShowLocationRepository {
    private AnyDoneService anyDoneService;

    public ShowLocationRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

/*    @Override
    public Observable<UserRpcProto.UserBaseResponse> makeLocationDefault(String token,
                                                                         String locationId) {
        return anyDoneService.makeLocationDefault(token, locationId);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> deleteLocation(String token,
                                                                    String locationId) {
        return anyDoneService.deleteLocation(token, locationId);
    }*/
}
