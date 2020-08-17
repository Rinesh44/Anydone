package com.treeleaf.anydone.serviceprovider.tickets.assignedtickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class AssignedTicketContract {
    public interface AssignedTicketView extends BaseView {

        void getAssignedTicketSuccess();

        void getAssignedTicketFail(String msg);
    }

    public interface AssignedTicketPresenter extends Presenter<AssignedTicketView> {

        void getAssignedTickets(boolean showProgress, long from, long to, int page);
    }
}
