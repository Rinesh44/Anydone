package com.anydone.desk.picklocation;

import com.anydone.desk.rest.service.AnyDoneService;

public class PickLocationRepositoryImpl implements PickLocationRepository {
    AnyDoneService service;

    public PickLocationRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }
}

