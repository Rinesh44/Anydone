package com.anydone.desk.threads.threadanalytics;

import com.anydone.desk.base.presenter.BasePresenter;

import javax.inject.Inject;

public class AnalyticsPresenterImpl  extends BasePresenter<AnalyticsContract.AnalyticsView>
implements AnalyticsContract.AnalyticsPresenter {
    private AnalyticsRepository analyticsRepository;
    private static final String TAG = "AnalyticsPresenterImpl";

    @Inject
    public AnalyticsPresenterImpl(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }
}
