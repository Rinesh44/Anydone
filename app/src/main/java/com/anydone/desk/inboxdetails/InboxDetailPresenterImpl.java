package com.anydone.desk.inboxdetails;

import com.anydone.desk.base.presenter.BasePresenter;

import javax.inject.Inject;

public class InboxDetailPresenterImpl extends BasePresenter<InboxDetailContract.InboxDetailView>
        implements InboxDetailContract.InboxDetailsPresenter {
    private InboxDetailsRepository inboxDetailsRepository;

    @Inject
    public InboxDetailPresenterImpl(InboxDetailsRepository inboxDetailsRepository) {
        this.inboxDetailsRepository = inboxDetailsRepository;
    }
}
