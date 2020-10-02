package com.treeleaf.anydone.serviceprovider.addticket;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import java.util.List;

public class AddTicketContract {
    public interface AddTicketView extends BaseView {

        void onCreateTicketSuccess();

        void onCreateTicketFail(String msg);

        void onInvalidSummary();

        void onInvalidDesc();

        void onInvalidCustomer();

        void onInvalidPhone();

        void onInvalidEmail();

    }

    public interface AddTicketPresenter extends Presenter<AddTicketView> {
        void createTicket(String title, String description, String customerId, String customerEmail,
                          String customerPhone, String customerName, List<String> tags, String assignedEmployeeId,
                          int priority, TicketProto.TicketSource ticketSource);

    }
}
