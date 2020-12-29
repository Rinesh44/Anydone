package com.treeleaf.anydone.serviceprovider.alltickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class AllTicketContract {
    public interface AllTicketView extends BaseView {

        void getAllTicketSuccess();

        void getAllTicketFail(String msg);

        void updateTickets(List<Tickets> ticketsList);

        void filterTicketsFailed(String msg);

        void onSubscribeSuccess(long ticketId);

        void onSubscribeFail(String msg);

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

        void assignSuccess();

        void assignFail(String msg);
    }

    public interface AllTicketPresenter extends Presenter<AllTicketContract.AllTicketView> {

        void getAllTickets(boolean showProgress, long from, long to, int page);

        void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority);

        void subscribe(long ticketId);

        void getEmployees();

        void assignTicket(long ticketId, String employeeId);

    }
}
