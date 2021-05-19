package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketattachment;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class TicketAttachmentRepositoryImpl implements TicketAttachmentRepository {
    AnyDoneService anyDoneService;

    public TicketAttachmentRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
