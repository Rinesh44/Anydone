package com.treeleaf.anydone.serviceprovider.ticketdetails;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class TicketDetailsRepositoryImpl implements TicketDetailsRepository {
    private AnyDoneService anyDoneService;

    public TicketDetailsRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
