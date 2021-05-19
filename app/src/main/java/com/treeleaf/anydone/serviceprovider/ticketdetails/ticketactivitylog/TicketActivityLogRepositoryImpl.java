package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketactivitylog;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class TicketActivityLogRepositoryImpl implements TicketActivityLogRepository {
    AnyDoneService anyDoneService;

    public TicketActivityLogRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
