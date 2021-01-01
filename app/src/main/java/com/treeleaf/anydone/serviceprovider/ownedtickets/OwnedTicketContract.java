package com.treeleaf.anydone.serviceprovider.ownedtickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class OwnedTicketContract {
    public interface OwnedTicketView extends BaseView {

        void getOwnedTicketSuccess();

        void getOwnedTicketFail(String msg);

        void updateTickets(List<Tickets> ticketsList);

        void filterTicketsFailed(String msg);
    }

    public interface OwnedTicketPresenter extends Presenter<OwnedTicketContract.OwnedTicketView> {

        void getOwnedTickets(boolean showProgress, long from, long to, int page);

        void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority);

    }
}
