package com.anydone.desk.threaddetails.threadfrontholder.threadactivitylog;

import com.anydone.desk.R;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;

public class ThreadActivityLogFragment extends BaseFragment<ThreadActivityLogPresenterImpl>
implements ThreadActivityLogContract.ThreadActivityLogView {

    @Override
    protected int getLayout() {
        return R.layout.fragment_thread_activity_log;
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
