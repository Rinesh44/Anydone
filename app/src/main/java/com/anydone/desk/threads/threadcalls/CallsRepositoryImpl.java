package com.anydone.desk.threads.threadcalls;

import com.anydone.desk.rest.service.AnyDoneService;

public class CallsRepositoryImpl implements CallsRepository {
    AnyDoneService anyDoneService;

    public CallsRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
