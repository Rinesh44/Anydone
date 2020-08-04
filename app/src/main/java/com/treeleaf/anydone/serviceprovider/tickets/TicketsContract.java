package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.FilterObject;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class TicketsContract {

    public interface TicketsView extends BaseView {

        void getTicketsSuccess();

        void getTicketsFail(String msg);

        void onSubscribeTicketsSeparated(List<Tickets> subscribeTicketList);

        void onAllTicketsSeparated(List<Tickets> allTicketList);

    }

    public interface TicketsPresenter extends Presenter<TicketsView> {

        void getTickets(boolean showProgress);

        void separateSubscribeAndAllTickets(List<Tickets> ticketsList, int fragmentIndex,
                                            boolean filter);
    }
}
