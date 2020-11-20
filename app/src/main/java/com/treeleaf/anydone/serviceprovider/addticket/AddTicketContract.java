package com.treeleaf.anydone.serviceprovider.addticket;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;

import java.util.List;

public class AddTicketContract {
    public interface AddTicketView extends BaseView {

        void onCreateTicketSuccess();

        void onCreateTicketFail(String msg);

        void onInvalidSummary();

        void onInvalidDesc();

        void onInvalidCustomer();

        void onInvalidTicketType();

        void onInvalidPhone();

        void onInvalidEmail();

    }

    public interface AddTicketPresenter extends Presenter<AddTicketView> {
        void createTicket(String ticketType, String title, String description, String customerId,
                          String customerEmail, String customerPhone, String customerName,
                          String customerPic,
                          List<String> tags, List<Label> ticketLabels, String estimatedTime,
                          String assignedEmployeeId, int priority,
                          TicketProto.TicketSource ticketSource, boolean customerAsSelf,
                          String refId);
    }
}
