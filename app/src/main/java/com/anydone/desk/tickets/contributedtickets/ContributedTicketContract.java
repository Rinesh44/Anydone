package com.anydone.desk.tickets.contributedtickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class ContributedTicketContract {

    public interface ContributedTicketView extends BaseView {

        void getContributedTicketSuccess();

        void getContributedTicketFail(String msg);
    }

    public interface ContributedTicketPresenter extends Presenter<ContributedTicketView> {

        void getContributedTickets(boolean showProgress, long from, long to, int page);

    }
}
