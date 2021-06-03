package com.anydone.desk.threaddetails.threadfrontholder.threadactivitylog;

import com.anydone.desk.base.presenter.BasePresenter;

import javax.inject.Inject;

public class ThreadActivityLogPresenterImpl extends BasePresenter<ThreadActivityLogContract.ThreadActivityLogView>
        implements ThreadActivityLogContract.ThreadActivityLogPresenter {

    private ThreadActivityLogRepository threadActivityLogRepository;

    @Inject
    public ThreadActivityLogPresenterImpl(ThreadActivityLogRepository threadActivityLogRepository) {
        this.threadActivityLogRepository = threadActivityLogRepository;
    }
}
