package com.anydone.desk.ticketdetails.ticketattachment;

import com.anydone.desk.rest.service.AnyDoneService;

public class TicketAttachmentRepositoryImpl implements TicketAttachmentRepository {
    AnyDoneService anyDoneService;

    public TicketAttachmentRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
