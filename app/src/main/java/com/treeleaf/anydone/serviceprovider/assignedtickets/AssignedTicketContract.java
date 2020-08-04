package com.treeleaf.anydone.serviceprovider.assignedtickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class AssignedTicketContract {
    public interface AssignedTicketsView extends BaseView {

        void getAssignedTicketsSuccess();

        void getAssignedTicketsFail(String msg);

        void onClosedTicketsSeparated(List<Tickets> closedTicketList);

        void onOpenTicketsSeparated(List<Tickets> openTicketList);

    }

    public interface AssignedTicketsPresenter extends Presenter<AssignedTicketContract.AssignedTicketsView> {

        void getAssignedTickets(boolean showProgress);

        void separateOpenAndClosedTickets(List<Tickets> ticketsList, int fragmentIndex,
                                            boolean filter);
    }
}
