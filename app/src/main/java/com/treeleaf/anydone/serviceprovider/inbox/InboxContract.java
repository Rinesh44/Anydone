package com.treeleaf.anydone.serviceprovider.inbox;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class InboxContract {
    public interface InboxView extends BaseView {
        void getServicesSuccess();

        void getServicesFail(String msg);

        void getInboxMessageSuccess();

        void getInboxMessageFail(String msg);

    }

    public interface InboxPresenter extends Presenter<InboxContract.InboxView> {
        void getServices();

        void getInboxMessages();
    }
}
