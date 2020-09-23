package com.treeleaf.anydone.serviceprovider.dashboard;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class DashboardRepositoryImpl implements DashboardRepository {
    private AnyDoneService anyDoneService;

    public DashboardRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
