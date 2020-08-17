package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class TicketsPresenterImpl extends BasePresenter<TicketsContract.TicketsView> implements
        TicketsContract.TicketsPresenter {
    private static final String TAG = "TicketsPresenterImpl";

    private TicketsRepository ticketsRepository;

    @Inject
    public TicketsPresenterImpl(TicketsRepository ticketsRepository) {
        this.ticketsRepository = ticketsRepository;
    }
}
