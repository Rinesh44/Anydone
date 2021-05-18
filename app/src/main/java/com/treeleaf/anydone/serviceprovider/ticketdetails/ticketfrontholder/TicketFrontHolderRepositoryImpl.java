package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketfrontholder;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import javax.inject.Inject;

public class TicketFrontHolderRepositoryImpl implements TicketFrontHolderRepository {
    AnyDoneService anyDoneService;

    public TicketFrontHolderRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
