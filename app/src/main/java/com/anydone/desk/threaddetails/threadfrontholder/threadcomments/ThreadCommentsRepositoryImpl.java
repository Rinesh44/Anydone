package com.anydone.desk.threaddetails.threadfrontholder.threadcomments;

import com.anydone.desk.rest.service.AnyDoneService;

public class ThreadCommentsRepositoryImpl implements ThreadCommentsRepository {
    AnyDoneService anyDoneService;

    public ThreadCommentsRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
