package com.anydone.desk.tickets.unassignedtickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.model.Priority;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.TicketCategory;
import com.anydone.desk.realm.model.Tickets;

import java.util.List;

public class UnassignedTicketsContract {
    public interface UnassignedView extends BaseView {
        void getAssignableTicketSuccess();

        void getAssignableTicketFail(String msg);

        void assignSuccess();

        void assignFail(String msg);

        void updateAssignableTickets(List<Tickets> ticketsList);

        void filterAssignableTicketFailed(String msg);

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

        void showProgressExport();

        void onExportSuccess(String url, String fileType);

        void onExportFail(String msg);
    }


    public interface UnassignedPresenter extends Presenter<UnassignedView> {
        void getAssignableTickets(boolean showProgress, long from, long to, int pageSize);

        void assignTicket(long ticketId, String employeeId);

        void filterAssignableTickets(String searchQuery, long from, long to, int ticketState, Priority priority,
                                     AssignEmployee selectedEmp, TicketCategory
                                             selectedTicketCategory, Tags selectedTeam,
                                     Service selectedService);

        void getEmployees();

        void export(String searchQuery, long from, long to, int ticketState, Priority priority,
                    AssignEmployee selectedEmp,
                    TicketCategory selectedTicketType, Tags selectedTeam,
                    Service selectedService, String reqType, String repType);

    }

}
