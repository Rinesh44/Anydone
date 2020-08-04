package com.treeleaf.anydone.serviceprovider.picklocation;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class PickLocationRepositoryImpl implements PickLocationRepository {
    AnyDoneService service;

    public PickLocationRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }
}

