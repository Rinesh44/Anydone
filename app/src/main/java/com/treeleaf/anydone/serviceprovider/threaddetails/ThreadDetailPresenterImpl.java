package com.treeleaf.anydone.serviceprovider.threaddetails;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class ThreadDetailPresenterImpl extends BasePresenter<ThreadDetailContract.ThreadDetailView>
        implements ThreadDetailContract.ThreadDetailsPresenter {
    public final String PUBLISH_TOPIC = "anydone/rtc/relay";
    private static final String TAG = "TicketDetailsPresenterI";
    private ThreadDetailsRepository threadDetailsRepository;


    @Inject
    public ThreadDetailPresenterImpl(ThreadDetailsRepository threadDetailsRepository) {
        this.threadDetailsRepository = threadDetailsRepository;
    }
}
