package com.anydone.desk.ownedtickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.model.Priority;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.TicketCategory;
import com.anydone.desk.realm.model.Tickets;

import java.util.List;

public class OwnedTicketContract {
    public interface OwnedTicketView extends BaseView {

        void getOwnedTicketSuccess();

        void getOwnedTicketFail(String msg);

        void updateTickets(List<Tickets> ticketsList);

        void filterTicketsFailed(String msg);

        void onExportSuccess(String url, String fileType);

        void onExportFail(String msg);

        void showProgressExport();
    }

    public interface OwnedTicketPresenter extends Presenter<OwnedTicketContract.OwnedTicketView> {

        void getOwnedTickets(boolean showProgress, long from, long to, int page);

        void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority,
                           AssignEmployee selectedEmp, TicketCategory
                                   selectedTicketCategory, Tags selectedTeam,
                           Service selectedService);


        void export(String searchQuery, long from, long to, int ticketState, Priority priority,
                    AssignEmployee selectedEmp,
                    TicketCategory selectedTicketType, Tags selectedTeam,
                    Service selectedService, String reqType, String repType);

    }
}
