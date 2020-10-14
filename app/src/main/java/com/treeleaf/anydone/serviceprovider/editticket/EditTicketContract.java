package com.treeleaf.anydone.serviceprovider.editticket;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class EditTicketContract {
    public interface EditTicketView extends BaseView {

        void onTitleEditSuccess();

        void onTitleEditFail(String msg);

        void onDescEditSuccess();

        void onDescEditFail(String msg);

        void onEstimatedTimeEditSuccess();

        void onEstimatedTimeEditFail(String msg);
    }

    public interface EditTicketPresenter extends Presenter<EditTicketView> {
        void editTicketTitle(String ticketId, String title);

        void editTicketDesc(String ticketId, String desc);

        void editTicketEstimatedTime(String ticketId, String estimatedTime);

    }
}