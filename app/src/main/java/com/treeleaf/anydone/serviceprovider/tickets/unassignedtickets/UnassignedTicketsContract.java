package com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class UnassignedTicketsContract {
    public interface UnassignedView extends BaseView {
        void getAssignableTicketSuccess();

        void getAssignableTicketFail(String msg);

        void assignSuccess();

        void assignFail(String msg);

        void updateAssignableTickets(List<Tickets> ticketsList);

        void filterAssignableTicketFailed(String msg);

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);
    }


    public interface UnassignedPresenter extends Presenter<UnassignedView> {
        void getAssignableTickets(boolean showProgress, long from, long to, int pageSize);

        void assignTicket(long ticketId, String employeeId);

        void filterAssignableTickets(String searchQuery, long from, long to, int ticketState, Priority priority);

        void getEmployees();

    }

}
