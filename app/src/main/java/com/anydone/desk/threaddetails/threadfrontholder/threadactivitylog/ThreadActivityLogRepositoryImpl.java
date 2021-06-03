package com.anydone.desk.threaddetails.threadfrontholder.threadactivitylog;

import com.anydone.desk.rest.service.AnyDoneService;

public class ThreadActivityLogRepositoryImpl implements  ThreadActivityLogRepository{
    AnyDoneService anyDoneService;

    public ThreadActivityLogRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
