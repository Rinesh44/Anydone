package com.anydone.desk.addticket;

import com.treeleaf.anydone.entities.TicketProto;
import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.DependentTicket;
import com.anydone.desk.realm.model.Label;

import java.util.List;

public class AddTicketContract {
    public interface AddTicketView extends BaseView {

        void onCreateTicketSuccess();

        void onCreateTicketFail(String msg);

        void onInvalidSummary();

        void onInvalidDesc();

        void onInvalidCustomer();

        void onInvalidTicketType();

        void onInvalidEstTime();

        void onInvalidPhone();

        void onInvalidEmail();

        void findTagsSuccess();

        void findTagsFail(String msg);

        void getLabelSuccess();

        void getLabelFail(String msg);

        void findEmployeeSuccess();

        void findEmployeeFail(String msg);

        void findCustomerSuccess();

        void findCustomerFail(String msg);

        void getTypeSuccess();

        void getTypeFail(String msg);

        void getSummarySuggestionSuccess(TicketProto.TicketAutofillSuggestionRes autoFillResponse);

        void getSummarySuggestionFail(String msg);

        void getDependencyTicketsListSuccess();

        void getDependencyTicketsListFail(String msg);

        void searchDependentTicketSuccess(List<DependentTicket> ticketList);

        void searchDependentTicketFail(String msg);

    }

    public interface AddTicketPresenter extends Presenter<AddTicketView> {

        void findTags();

        void getLabels();

        void findEmployees();

        void findCustomers();

        void getTicketTypes();

        void getSummarySuggestions(String summary);

        void createTicket(String ticketType, String title, String description, String customerId,
                          String customerEmail, String customerPhone, String customerName,
                          String customerPic,
                          List<String> tags, List<Label> ticketLabels, String estimatedTime,
                          String assignedEmployeeId, int priority,
                          TicketProto.TicketSource ticketSource, boolean customerAsSelf,
                          String refId, DependentTicket dependentTicket);

        void getDependencyListTickets();

        void searchTickets(String query);
    }


}
