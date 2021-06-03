package com.anydone.desk.threaddetails.threadfrontholder;

import com.anydone.desk.base.presenter.BasePresenter;

import javax.inject.Inject;

public class ThreadFrontHolderPresenterImpl extends BasePresenter<ThreadFrontHolderContract.ThreadFrontHolderView>
        implements ThreadFrontHolderContract.ThreadFrontHolderPresenter {
    private ThreadFrontHolderRepository threadFrontHolderRepository;

    @Inject
    public ThreadFrontHolderPresenterImpl(ThreadFrontHolderRepository threadFrontHolderRepository) {
        this.threadFrontHolderRepository = threadFrontHolderRepository;
    }
}
