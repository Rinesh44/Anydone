package com.anydone.desk.tickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.model.Priority;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.TicketCategory;
import com.anydone.desk.realm.model.Tickets;

import java.util.List;

public class TicketsContract {

    public interface TicketsView extends BaseView {
        void updatePendingTicketList();

        void filterPendingTicketsFailed(String msg);

        void updateInProgressTicketList();

        void filterInProgressTicketFailed(String msg);

        void updateClosedTicketList();

        void filterClosedTicketFailed(String msg);

        void getServiceSuccess();

        void getServiceFail(String msg);

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

        void getTicketTypeSuccess();

        void getTicketTypeFail(String msg);

        void getTeamSuccess();

        void getTeamFail(String msg);

        void findCustomersSuccess();

        void findCustomersFail(String msg);

    }

    public interface TicketsPresenter extends Presenter<TicketsView> {
        void getServices();

        void filterPendingTickets(String searchQuery, long from, long to, int ticketState,
                                  int priority, AssignEmployee assignEmployee, TicketCategory
                                          ticketCategory, Tags tags, Service service, Customer customer);

        void filterInProgressTickets(String searchQuery, long from, long to, int ticketState,
                                     int priority, AssignEmployee assignEmployee, TicketCategory
                                             ticketCategory, Tags tags, Service service, Customer customer);

        void filterClosedTickets(String searchQuery, long from, long to, int ticketState,
                                 int priority, AssignEmployee assignEmployee, TicketCategory
                                         ticketCategory, Tags tags, Service service, Customer customer);

        void findEmployees();

        void findTicketTypes();

        void findTeams();

        void findCustomers();
    }
}
