package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;

import java.util.List;

import io.realm.RealmList;

public class TicketTimelineContract {

    public interface TicketTimelineView extends BaseView {

        void getTicketTimelineSuccess(Employee assignedEmployee);

        void geTicketTimelineFail(String msg);

        void setCustomerDetails(Customer customerDetails);

        void setAssignedEmployee(Employee assignedEmployee);

        void onEmployeeUnAssignSuccess(String empId);

        void onEmployeeUnAssignFail(String msg);

        void onTicketCloseSuccess();

        void onTicketCloseFail(String msg);

        void onTicketReopenSuccess();

        void onTicketReopenFail(String msg);

        void onTicketResolveSuccess();

        void onTicketResolveFail(String msg);

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

    }

    public interface TicketTimelinePresenter extends Presenter<TicketTimelineView> {

        void getTicketTimeline(long ticketId);

        void getCustomerDetails(long ticketId);

        void getAssignedEmployees(long ticketId);

//        void unAssignEmployee(long ticketId, String employeeId);

        void closeTicket(long ticketId);

        void reopenTicket(long ticketId);

        void resolveTicket(long ticketId);

        void getEmployees();

    }
}
