package com.anydone.desk.threaddetails.threadfrontholder.threadcomments;

import com.anydone.desk.base.presenter.BasePresenter;

import javax.inject.Inject;

public class ThreadCommentsPresenterImpl extends BasePresenter<ThreadCommentsContract.ThreadCommentsView>
        implements ThreadCommentsContract.ThreadCommentsPresenter {
    private ThreadCommentsRepository threadCommentsRepository;

    @Inject
    public ThreadCommentsPresenterImpl(ThreadCommentsRepository threadCommentsRepository) {
        this.threadCommentsRepository = threadCommentsRepository;
    }
}
