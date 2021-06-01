package com.anydone.desk.threads.threadcalls;

import com.anydone.desk.base.presenter.BasePresenter;

import javax.inject.Inject;

public class CallsPresenterImpl extends BasePresenter<CallsContract.CallsView>
        implements CallsContract.CallsPresenter {

    private CallsRepository callsRepository;
    private static final String TAG = "CallsPresenterImpl";

    @Inject
    public CallsPresenterImpl(CallsRepository callsRepository) {
        this.callsRepository = callsRepository;
    }
}
