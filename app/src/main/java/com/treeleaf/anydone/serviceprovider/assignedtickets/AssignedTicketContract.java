package com.treeleaf.anydone.serviceprovider.assignedtickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.opentickets.OpenTicketContract;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class AssignedTicketContract {
    public interface AssignedTicketView extends BaseView {

        void getAssignedTicketSuccess();

        void getAssignedTicketFail(String msg);

        void updateTickets(List<Tickets> ticketsList);

        void filterTicketsFailed(String msg);
    }

    public interface AssignedTicketPresenter extends Presenter<AssignedTicketView> {

        void getAssignedTickets(boolean showProgress, long from, long to, int page);

        void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority,
                           AssignEmployee selectedEmp,
                           TicketCategory selectedTicketType, Tags selectedTeam,
                           Service selectedService);

    }
}
