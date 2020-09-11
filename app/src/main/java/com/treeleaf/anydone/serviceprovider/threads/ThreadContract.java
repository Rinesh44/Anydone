package com.treeleaf.anydone.serviceprovider.threads;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class ThreadContract {

    public interface ThreadView extends BaseView {
        void getConversationThreadSuccess();

        void getConversationThreadFail(String msg);

        void getServiceSuccess();

        void getServiceFail(String msg);
    }

    public interface ThreadPresenter extends Presenter<ThreadView> {
        void getConversationThreads(boolean showProgress);

        void getServices();
    }
}
