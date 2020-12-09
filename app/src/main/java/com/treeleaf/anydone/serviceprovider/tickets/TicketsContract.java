package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class TicketsContract {

    public interface TicketsView extends BaseView {
        void updatePendingTicketList(List<Tickets> ticketsList);

        void filterPendingTicketsFailed(String msg);

        void updateInProgressTicketList(List<Tickets> ticketsList);

        void filterInProgressTicketFailed(String msg);

        void updateClosedTicketList(List<Tickets> ticketsList);

        void filterClosedTicketFailed(String msg);

        void getServiceSuccess();

        void getServiceFail(String msg);

    }

    public interface TicketsPresenter extends Presenter<TicketsView> {
        void getServices();

        void filterPendingTickets(String searchQuery, long from, long to, int ticketState,
                                  Priority priority);

        void filterInProgressTickets(String searchQuery, long from, long to, int ticketState,
                                     Priority priority);

        void filterClosedTickets(String searchQuery, long from, long to, int ticketState,
                                 Priority priority);
    }
}
