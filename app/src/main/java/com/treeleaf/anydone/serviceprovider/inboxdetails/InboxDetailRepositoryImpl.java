package com.treeleaf.anydone.serviceprovider.inboxdetails;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class InboxDetailRepositoryImpl implements InboxDetailsRepository {
    private AnyDoneService anyDoneService;

    public InboxDetailRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
