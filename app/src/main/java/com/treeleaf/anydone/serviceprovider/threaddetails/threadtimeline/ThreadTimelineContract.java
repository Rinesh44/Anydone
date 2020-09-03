package com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;

public class ThreadTimelineContract {

    public interface ThreadTimelineView extends BaseView {

        void getTicketTimelineSuccess(Employee assignedEmployee);

        void geTicketTimelineFail(String msg);

        void setCustomerDetails(Customer customerDetails);

        void setAssignedEmployee(Employee assignedEmployee);

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

    }

    public interface ThreadTimelinePresenter extends Presenter<ThreadTimelineView> {

        void getTicketTimeline(long ticketId);

        void getCustomerDetails(long ticketId);

        void getAssignedEmployees(long ticketId);

        void getEmployees();

    }
}
