package com.treeleaf.anydone.serviceprovider.ticketdetails;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class TicketDetailsPresenterImpl extends BasePresenter<TicketDetailsContract.TicketDetailsView>
        implements TicketDetailsContract.TicketDetailsPresenter {
    private static final String TAG = "TicketDetailsPresenterI";
    private TicketDetailsRepository ticketDetailsRepository;


    @Inject
    public TicketDetailsPresenterImpl(TicketDetailsRepository ticketDetailsRepository) {
        this.ticketDetailsRepository = ticketDetailsRepository;
    }

}