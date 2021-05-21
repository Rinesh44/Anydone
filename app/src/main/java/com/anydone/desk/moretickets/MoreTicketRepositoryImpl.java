package com.anydone.desk.moretickets;

import com.anydone.desk.rest.service.AnyDoneService;

public class MoreTicketRepositoryImpl implements MoreTicketRepository {
    AnyDoneService anyDoneService;

    public MoreTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
