package com.anydone.desk.ticketdetails.ticketactivitylog;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class TicketActivityLogContract {

    public interface TicketActivityLogView extends BaseView {
        void getActivityLogSuccess();

        void getActivityLogFail(String msg);

    }

    public interface TicketActivityLogPresenter extends Presenter<TicketActivityLogView> {
        void getActivityLog(String ticketId, boolean showProgress);
    }
}

