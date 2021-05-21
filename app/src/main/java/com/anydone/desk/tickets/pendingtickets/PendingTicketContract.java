package com.anydone.desk.tickets.pendingtickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class PendingTicketContract {
    public interface PendingTicketView extends BaseView {

        void getPendingTicketSuccess();

        void getPendingTicketFail(String msg);

        void showEmptyView();

        void onTicketStartSuccess(long ticketId, long estimatedTime);

        void onTicketStartFail(String msg);
    }

    public interface PendingTicketPresenter extends Presenter<PendingTicketView> {

        void getPendingTickets(boolean showProgress, long from, long to, int page);

        void startTicket(long ticketId);
    }
}
