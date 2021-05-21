package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketactivitylog;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class TicketActivityLogContract {

    public interface TicketActivityLogView extends BaseView {
        void getActivityLogSuccess();

        void getActivityLogFail(String msg);

    }

    public interface TicketActivityLogPresenter extends Presenter<TicketActivityLogView> {
        void getActivityLog(String ticketId, boolean showProgress);
    }
}

