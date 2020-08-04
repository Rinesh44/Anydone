package com.treeleaf.anydone.serviceprovider.landing;


import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class LandingContract {

    public interface LandingView extends BaseView {

    }

    public interface LandingPresenter extends Presenter<LandingView> {

    }

}
