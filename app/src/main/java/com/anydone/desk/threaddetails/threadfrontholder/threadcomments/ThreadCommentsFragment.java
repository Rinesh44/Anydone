package com.anydone.desk.threaddetails.threadfrontholder.threadcomments;

import com.anydone.desk.R;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;

public class ThreadCommentsFragment extends BaseFragment<ThreadCommentsPresenterImpl>
implements ThreadCommentsContract.ThreadCommentsView {
    @Override
    protected int getLayout() {
        return R.layout.fragment_thread_comments;
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
