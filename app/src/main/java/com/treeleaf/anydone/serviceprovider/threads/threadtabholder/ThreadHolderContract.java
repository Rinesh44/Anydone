package com.treeleaf.anydone.serviceprovider.threads.threadtabholder;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class ThreadHolderContract {
    public interface ThreadHolderView extends BaseView {
        void getServiceSuccess();

        void getServiceFail(String msg);
    }

    public interface ThreadHolderPresenter extends Presenter<ThreadHolderView> {
        void getServices();

    }
}
