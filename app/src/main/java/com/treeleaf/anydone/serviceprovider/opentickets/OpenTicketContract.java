package com.treeleaf.anydone.serviceprovider.opentickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;

import java.util.List;

public class OpenTicketContract {
    public interface OpenTicketView extends BaseView {

        void getOpenTicketSuccess();

        void getOpenTicketFail(String msg);

        void updateTickets(List<Tickets> ticketsList);

        void filterTicketsFailed(String msg);

        void onExportSuccess(String url, String fileType);

        void onExportFail(String msg);

        void showProgressExport();
    }

    public interface OpenTicketPresenter extends Presenter<OpenTicketContract.OpenTicketView> {

        void getOpenTickets(boolean showProgress, long from, long to, int page);

        void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority,
                           AssignEmployee selectedEmp,
                           TicketCategory selectedTicketType, Tags selectedTeam,
                           Service selectedService);


        void export(String searchQuery, long from, long to, int ticketState, Priority priority,
                    AssignEmployee selectedEmp,
                    TicketCategory selectedTicketType, Tags selectedTeam,
                    Service selectedService, String reqType, String repType);

    }
}
