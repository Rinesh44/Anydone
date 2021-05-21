package com.anydone.desk.ticketdetails.ticketactivitylog;

import com.anydone.desk.rest.service.AnyDoneService;

public class TicketActivityLogRepositoryImpl implements TicketActivityLogRepository {
    AnyDoneService anyDoneService;

    public TicketActivityLogRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
