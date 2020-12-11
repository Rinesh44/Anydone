package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;

import java.util.List;

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

        void assignSuccess(String empId);

        void assignFail(String msg);

        void showProgressEmployee();

        void hideProgressEmployee();

        void enableBotSuccess();

        void enableBotFail(String msg);

        void disableBotFail(String msg);

        void disableBotSuccess();

        void onPriorityEditSuccess(int priority);

        void onPriorityEditFail(String msg);

        void onEditTeamSuccess();

        void onEditTeamFail(String msg);

        void onEditLabelSuccess();

        void onEditLabelFail(String msg);

        void onTicketTypeEditSuccess();

        void onTicketTypeEditFail(String msg);

        void getTypeSuccess();

        void getTypeFail(String msg);

        void onTaskStartSuccess(long estTime);

        void onTaskStartFail(String msg);

    }

    public interface TicketTimelinePresenter extends Presenter<TicketTimelineView> {
        void getTicketTypes();

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

        void editTicketPriority(String ticketId, int priority);

        void editTeam(String ticketId, List<String> tags);

        void editLabel(String ticketId, List<Label> labels);

        void editTicketType(String ticketId, String ticketTypeId, String ticketType);

        void startTask(long ticketId);

    }
}
