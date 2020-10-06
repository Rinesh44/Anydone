package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;

public class TicketTimelineContract {

    public interface TicketTimelineView extends BaseView {

        void getTicketTimelineSuccess(AssignEmployee assignedEmployee);

        void geTicketTimelineFail(String msg);

        void setCustomerDetails(Customer customerDetails);

        void setAssignedEmployee(AssignEmployee assignedEmployee);

        void onContributorUnAssignSuccess(String empId);

        void onContributorUnAssignFail(String msg);

        void onTicketCloseSuccess();

        void onTicketCloseFail(String msg);

        void onTicketReopenSuccess();

        void onTicketReopenFail(String msg);

        void onTicketResolveSuccess();

        void onTicketResolveFail(String msg);

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

        void assignSuccess();

        void assignFail(String msg);

        void showProgressEmployee();

        void hideProgressEmployee();

        void enableBotSuccess();

        void enableBotFail(String msg);

        void disableBotFail(String msg);

        void disableBotSuccess();

    }

    public interface TicketTimelinePresenter extends Presenter<TicketTimelineView> {

        void getTicketTimeline(long ticketId);

        void getCustomerDetails(long ticketId);

        void getAssignedEmployees(long ticketId);

        void unAssignContributor(long ticketId, String contributorId);

        void closeTicket(long ticketId);

        void reopenTicket(long ticketId);

        void resolveTicket(long ticketId);

        void getEmployees();

        void assignTicket(long ticketId, String employeeId);

        void enableBot(String ticketId);

        void disableBot(String ticketId);

    }
}
