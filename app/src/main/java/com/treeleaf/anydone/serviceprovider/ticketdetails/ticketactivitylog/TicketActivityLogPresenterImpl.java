package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketactivitylog;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class TicketActivityLogPresenterImpl extends BasePresenter<TicketActivityLogContract.TicketActivityLogView>
        implements TicketActivityLogContract.TicketActivityLogPresenter {
    private TicketActivityLogRepository ticketActivityLogRepository;


    @Inject
    public TicketActivityLogPresenterImpl(TicketActivityLogRepository ticketActivityLogRepository) {
        this.ticketActivityLogRepository = ticketActivityLogRepository;
    }
}
