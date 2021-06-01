package com.anydone.desk.threads.threadcalls;

import com.anydone.desk.R;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;

public class CallsFragment extends BaseFragment<CallsPresenterImpl>
implements CallsContract.CallsView {
    @Override
    protected int getLayout() {
        return R.layout.fragment_calls;
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
