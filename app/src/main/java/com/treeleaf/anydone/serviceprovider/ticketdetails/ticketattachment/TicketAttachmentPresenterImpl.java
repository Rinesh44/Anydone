package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketattachment;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class TicketAttachmentPresenterImpl extends BasePresenter<TicketAttachmentContract.TicketAttachmentView>
        implements TicketAttachmentContract.TicketAttachmentPresenter {
    private TicketAttachmentRepository ticketAttachmentRepository;

    @Inject
    public TicketAttachmentPresenterImpl(TicketAttachmentRepository ticketAttachmentRepository) {
        this.ticketAttachmentRepository = ticketAttachmentRepository;
    }
}
