package com.anydone.desk.tickets.unsubscribedtickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.model.Priority;
import com.anydone.desk.realm.model.Tickets;

import java.util.List;

public class UnsubscribedTicketContract {
    public interface UnsubscribedView extends BaseView {
        void getSubscribeableTicketSuccess();

        void getSubscribeableTicketFail(String msg);

        void onSubscribeSuccess(long ticketId);

        void onSubscribeFail(String msg);

        void updateTickets(List<Tickets> ticketsList);

        void filterTicketsFailed(String msg);
    }

    public interface UnsubscribedPresenter extends Presenter<UnsubscribedView> {
        void getSubscribeableTickets(boolean showProgress, long from, long to, int pageSize);

        void subscribe(long ticketId);

        void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority);
    }

}
