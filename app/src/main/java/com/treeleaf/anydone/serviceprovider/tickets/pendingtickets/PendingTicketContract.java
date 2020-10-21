package com.treeleaf.anydone.serviceprovider.tickets.pendingtickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class PendingTicketContract {
    public interface PendingTicketView extends BaseView {

        void getPendingTicketSuccess();

        void getPendingTicketFail(String msg);

        void showEmptyView();
    }

    public interface PendingTicketPresenter extends Presenter<PendingTicketView> {

        void getPendingTickets(boolean showProgress, long from, long to, int page);
    }
}
