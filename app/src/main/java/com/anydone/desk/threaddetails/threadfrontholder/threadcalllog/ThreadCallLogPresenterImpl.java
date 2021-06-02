package com.anydone.desk.threaddetails.threadfrontholder.threadcalllog;

import com.anydone.desk.base.presenter.BasePresenter;

import javax.inject.Inject;

public class ThreadCallLogPresenterImpl extends BasePresenter<ThreadCallLogContract.ThreadCallLogView>
implements ThreadCallLogContract.ThreadCallLogPresenter {
    private ThreadCallLogRepository threadCallLogRepository;

    @Inject
    public ThreadCallLogPresenterImpl(ThreadCallLogRepository threadCallLogRepository) {
        this.threadCallLogRepository = threadCallLogRepository;
    }
}
