package com.anydone.desk.threaddetails.threadfrontholder;

import com.anydone.desk.rest.service.AnyDoneService;

public class ThreadFrontHolderRepositoryImpl implements ThreadFrontHolderRepository{
    AnyDoneService anyDoneService;

    public ThreadFrontHolderRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
