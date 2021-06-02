package com.anydone.desk.threaddetails.threadfrontholder.threadcalllog;

import com.anydone.desk.rest.service.AnyDoneService;

public class ThreadCallLogRepositoryImpl implements ThreadCallLogRepository {
    AnyDoneService anyDoneService;

    public ThreadCallLogRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
