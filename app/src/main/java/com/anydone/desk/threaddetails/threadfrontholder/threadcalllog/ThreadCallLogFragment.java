package com.anydone.desk.threaddetails.threadfrontholder.threadcalllog;

import com.anydone.desk.R;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.threaddetails.threadfrontholder.threadactivitylog.ThreadActivityLogPresenterImpl;

public class ThreadCallLogFragment extends BaseFragment<ThreadActivityLogPresenterImpl>
        implements ThreadCallLogContract.ThreadCallLogView {
    @Override
    protected int getLayout() {
        return R.layout.fragment_thread_call_log;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onFailure(String message) {

    }
}
