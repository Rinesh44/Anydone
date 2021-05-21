package com.anydone.desk.alltickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.model.Priority;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.TicketCategory;
import com.anydone.desk.realm.model.Tickets;

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

        void onExportSuccess(String url, String fileType);

        void onExportFail(String msg);

        void showProgressExport();

    }

    public interface AllTicketPresenter extends Presenter<AllTicketContract.AllTicketView> {

        void getAllTickets(boolean showProgress, long from, long to, int page);

        void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority,
                           AssignEmployee selectedEmp,
                           TicketCategory selectedTicketType, Tags selectedTeam,
                           Service selectedService);

        void subscribe(long ticketId);

        void getEmployees();

        void assignTicket(long ticketId, String employeeId);

        void export(String searchQuery, long from, long to, int ticketState, Priority priority,
                    AssignEmployee selectedEmp,
                    TicketCategory selectedTicketType, Tags selectedTeam,
                    Service selectedService, String reqType, String repType);
    }
}
