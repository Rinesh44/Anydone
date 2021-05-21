package com.anydone.desk.tickets.inprogresstickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class InProgressTicketContract {

    public interface InProgressTicketsView extends BaseView {
        void getInProgressTicketsSuccess();

        void getInProgressTicketsFail(String msg);

        void showEmptyView();

        void onCloseTicketSuccess(long ticketId);

        void onCloseTicketFail(String msg);

        void onResolveTicketSuccess(long ticketId);

        void onResolveTicketFail(String msg);
    }

    public interface InProgressTicketsPresenter extends Presenter<InProgressTicketsView> {
        void getInProgressTickets(boolean showProgress, long from, long to, int page);

        void closeTicket(long ticketId);

        void resolveTicket(long ticketId);
    }
}
