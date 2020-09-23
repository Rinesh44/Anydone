package com.treeleaf.anydone.serviceprovider.dashboard;

import com.treeleaf.anydone.serviceprovider.account.AccountRepository;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class DashboardPresenterImpl extends BasePresenter<DashboardContract.DashboardView>
        implements DashboardContract.DashboardPresenter {

    private static final String TAG = "DashboardPresenterImpl";
    private DashboardRepository dashboardRepository;

    @Inject
    public DashboardPresenterImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }
}
