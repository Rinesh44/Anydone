package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class TicketsContract {

    public interface TicketsView extends BaseView {
        void updateAssignedTicketList(List<Tickets> ticketsList);

        void filterAssignedTicketsFailed(String msg);

        void updateSubscribedTicketList(List<Tickets> ticketsList);

        void filterSubscribedTicketFailed(String msg);

        void updateClosedTicketList(List<Tickets> ticketsList);

        void filterClosedTicketFailed(String msg);

        void getServiceSuccess();

        void getServiceFail(String msg);
    }

    public interface TicketsPresenter extends Presenter<TicketsView> {
        void filterAssignedTickets(String searchQuery, long from, long to, int ticketState);

        void filterSubscribedTickets(String searchQuery, long from, long to, int ticketState);

        void filterClosedTickets(String searchQuery, long from, long to, int ticketState);

        void getServices();
    }
}
