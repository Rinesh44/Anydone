package com.anydone.desk.ticketdetails.ticketfrontholder;

import com.anydone.desk.rest.service.AnyDoneService;

public class TicketFrontHolderRepositoryImpl implements TicketFrontHolderRepository {
    AnyDoneService anyDoneService;

    public TicketFrontHolderRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
