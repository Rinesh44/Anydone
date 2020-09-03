package com.treeleaf.anydone.serviceprovider.threads;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class ThreadPresenterImpl extends BasePresenter<ThreadContract.ThreadView> implements
        ThreadContract.ThreadPresenter {
    private ThreadRepository threadRepository;

    @Inject
    public ThreadPresenterImpl(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }
}
