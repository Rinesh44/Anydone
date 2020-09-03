package com.treeleaf.anydone.serviceprovider.threaddetails;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class ThreadDetailRepositoryImpl implements ThreadDetailsRepository {
    private AnyDoneService anyDoneService;

    public ThreadDetailRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
