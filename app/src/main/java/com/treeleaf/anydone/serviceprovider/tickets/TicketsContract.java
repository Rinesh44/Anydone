package com.treeleaf.anydone.serviceprovider.tickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.FilterObject;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class TicketsContract {

    public interface TicketsView extends BaseView {

        void getAssignedTicketSuccess();

        void getAssignedTicketFail(String msg);
    }

    public interface TicketsPresenter extends Presenter<TicketsView> {

        void getAssignedTickets(boolean showProgress, long from, long to, int page);
    }
}
