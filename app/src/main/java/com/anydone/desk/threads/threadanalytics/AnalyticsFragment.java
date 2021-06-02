package com.anydone.desk.threads.threadanalytics;

import com.anydone.desk.R;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;

public class AnalyticsFragment extends BaseFragment<AnalyticsPresenterImpl>
        implements AnalyticsContract.AnalyticsView {
    private static final String TAG = "AnalyticsFragment";

    @Override
    protected int getLayout() {
        return R.layout.fragment_analytics;
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
