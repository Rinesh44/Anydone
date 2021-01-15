package com.treeleaf.anydone.serviceprovider.moretickets;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class MoreTicketRepositoryImpl implements MoreTicketRepository {
    AnyDoneService anyDoneService;

    public MoreTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
