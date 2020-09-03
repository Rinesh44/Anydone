package com.treeleaf.anydone.serviceprovider.threaddetails;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class ThreadDetailContract {

    public interface ThreadDetailView extends BaseView {


    }

    public interface ThreadDetailsPresenter extends Presenter<ThreadDetailView> {

    }
}
