package com.treeleaf.anydone.serviceprovider.contributed;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class ContributedTicketContract {
    public interface ContributedTicketView extends BaseView {

        void getContributedTicketSuccess();

        void getContributedTicketFail(String msg);
    }

    public interface ContributedTicketPresenter extends Presenter<ContributedTicketView> {

        void getContributedTickets(boolean showProgress, long from, long to, int page);

    }
}
