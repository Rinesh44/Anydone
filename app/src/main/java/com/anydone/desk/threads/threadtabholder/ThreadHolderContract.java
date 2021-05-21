package com.anydone.desk.threads.threadtabholder;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class ThreadHolderContract {
    public interface ThreadHolderView extends BaseView {
        void getServiceSuccess();

        void getServiceFail(String msg);
    }

    public interface ThreadHolderPresenter extends Presenter<ThreadHolderView> {
        void getServices();

    }
}
