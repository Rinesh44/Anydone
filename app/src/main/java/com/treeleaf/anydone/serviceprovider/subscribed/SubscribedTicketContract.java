package com.treeleaf.anydone.serviceprovider.subscribed;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class SubscribedTicketContract {
    public interface SubscribeTicketsView extends BaseView {
        void getSubscribedTicketsSuccess();

        void getSubscribedTicketsFail(String msg);

        void onUnsubscribeSuccess(long ticketId);

        void onUnsubscribeFail(String msg);

        void updateTickets(List<Tickets> ticketsList);

        void filterTicketsFailed(String msg);
    }

    public interface SubscribeTicketsPresenter extends Presenter<SubscribeTicketsView> {
        void getSubscribedTickets(boolean showProgress, long from, long to, int page);

        void unsubscribeTicket(long ticketId);

        void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority,
                           AssignEmployee selectedEmp,
                           TicketCategory selectedTicketType, Tags selectedTeam,
                           Service selectedService);

    }
}
