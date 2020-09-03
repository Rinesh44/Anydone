package com.treeleaf.anydone.serviceprovider.threads;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class ThreadRepositoryImpl implements ThreadRepository {
    AnyDoneService service;

    public ThreadRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }
}
