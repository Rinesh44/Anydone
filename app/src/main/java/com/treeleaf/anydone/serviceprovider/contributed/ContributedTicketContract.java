package com.treeleaf.anydone.serviceprovider.contributed;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class ContributedTicketContract {
    public interface ContributedTicketView extends BaseView {

        void getContributedTicketSuccess();

        void getContributedTicketFail(String msg);

        void updateTickets(List<Tickets> ticketsList);

        void filterTicketsFailed(String msg);
    }

    public interface ContributedTicketPresenter extends Presenter<ContributedTicketView> {

        void getContributedTickets(boolean showProgress, long from, long to, int page);

        void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority
                , AssignEmployee selectedEmp, TicketCategory
                                   selectedTicketCategory, Tags selectedTeam,
                           Service selectedService);

    }
}
