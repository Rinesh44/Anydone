package com.treeleaf.anydone.serviceprovider.dashboard;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class DashboardContract {
    public interface DashboardView extends BaseView {

    }

    public interface DashboardPresenter extends Presenter<DashboardContract.DashboardView> {

    }
}
