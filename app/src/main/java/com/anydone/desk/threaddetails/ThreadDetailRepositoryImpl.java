package com.anydone.desk.threaddetails;

import com.anydone.desk.rest.service.AnyDoneService;

public class ThreadDetailRepositoryImpl implements ThreadDetailsRepository {
    private AnyDoneService anyDoneService;

    public ThreadDetailRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
