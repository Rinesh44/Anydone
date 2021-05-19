package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketfrontholder;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class TicketFrontHolderPresenterImpl extends BasePresenter<TicketFrontHolderContract.TicketFrontHolderView>
        implements TicketFrontHolderContract.TicketFrontHolderPresenter {
    private TicketFrontHolderRepository ticketFrontHolderRepository;

    @Inject
    public TicketFrontHolderPresenterImpl(TicketFrontHolderRepository ticketFrontHolderRepository) {
        this.ticketFrontHolderRepository = ticketFrontHolderRepository;
    }
}
