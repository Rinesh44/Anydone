package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import org.bouncycastle.asn1.cmc.TaggedAttribute;

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

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

        void getTicketTypeSuccess();

        void getTicketTypeFail(String msg);

        void getTeamSuccess();

        void getTeamFail(String msg);

    }

    public interface TicketsPresenter extends Presenter<TicketsView> {
        void getServices();

        void filterPendingTickets(String searchQuery, long from, long to, int ticketState,
                                  Priority priority, AssignEmployee assignEmployee, TicketCategory
                                          ticketCategory, Tags tags, Service service);

        void filterInProgressTickets(String searchQuery, long from, long to, int ticketState,
                                     Priority priority, AssignEmployee assignEmployee, TicketCategory
                                             ticketCategory, Tags tags, Service service);

        void filterClosedTickets(String searchQuery, long from, long to, int ticketState,
                                 Priority priority, AssignEmployee assignEmployee, TicketCategory
                                         ticketCategory, Tags tags, Service service);

        void findEmployees();

        void findTicketTypes();

        void findTeams();
    }
}
