package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;

import java.util.List;

import io.realm.RealmList;

public class TicketTimelineContract {

    public interface TicketTimelineView extends BaseView {

        void getTicketTimelineSuccess(RealmList<Employee> assignedEmployee);

        void geTicketTimelineFail(String msg);

        void setCustomerDetails(Customer customerDetails);

        void setAssignedEmployee(RealmList<Employee> assignedEmployee);

    }

    public interface TicketTimelinePresenter extends Presenter<TicketTimelineView> {

        void getTicketTimeline(long ticketId);

        void getCustomerDetails(long ticketId);

        void getAssignedEmployees(long ticketId);
    }
}
