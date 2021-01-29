package com.treeleaf.anydone.serviceprovider.inboxdetails;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class InboxDetailPresenterImpl extends BasePresenter<InboxDetailContract.InboxDetailView>
        implements InboxDetailContract.InboxDetailsPresenter {
    private InboxDetailsRepository inboxDetailsRepository;

    @Inject
    public InboxDetailPresenterImpl(InboxDetailsRepository inboxDetailsRepository) {
        this.inboxDetailsRepository = inboxDetailsRepository;
    }
}
