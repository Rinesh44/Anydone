package com.anydone.desk.inboxdetails;

import com.anydone.desk.rest.service.AnyDoneService;

public class InboxDetailRepositoryImpl implements InboxDetailsRepository {
    private AnyDoneService anyDoneService;

    public InboxDetailRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
