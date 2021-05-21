package com.anydone.desk.ticketdetails.tickettimeline;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.DependentTicket;
import com.anydone.desk.realm.model.Label;
import com.anydone.desk.realm.model.Tickets;

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

        void getTicketByIdSuccess(Tickets ticket);

        void getTicketByIdFail(String msg);

        void getDependencyTicketsListSuccess();

        void getDependencyTicketsListFail(String msg);

        void searchDependentTicketSuccess(List<DependentTicket> ticketList);

        void searchDependentTicketFail(String msg);

        void updateTicketSuccess(DependentTicket ticket, boolean isDelete);

        void updateTicketFail(String msg);

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

        void getTicketDetailsById(long ticketId);

        void getDependencyListTickets();

        void searchTickets(String query);

        void updateTicket(long ticketId, DependentTicket dependentTicket);
    }
}
