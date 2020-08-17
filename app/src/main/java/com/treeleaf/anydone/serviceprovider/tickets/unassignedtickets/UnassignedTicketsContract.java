package com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class UnassignedTicketsContract {
    public interface UnassignedView extends BaseView {
        void getAssignableTicketSuccess();

        void getAssignableTicketFail(String msg);

        void assignSuccess();

        void assignFail(String msg);
    }

    public interface UnassignedPresenter extends Presenter<UnassignedView> {
        void getAssignableTickets(long from, long to, int pageSize);

        void assignTicket(long ticketId, String employeeId);
    }

}
