package com.anydone.desk.tickets.closedresolvedtickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class ClosedTicketContract {
    public interface ClosedTicketView extends BaseView {

        void getClosedTicketSuccess();

        void getClosedTicketFail(String msg);

        void onReopenSuccess(long ticketId);

        void onReopenFail(String msg);

        void showEmptyView();
    }

    public interface ClosedTicketPresenter extends Presenter<ClosedTicketView> {

        void getClosedResolvedTickets(boolean showProgress, long from, long to, int page);

        void reopenTicket(long ticketId);
    }
}
