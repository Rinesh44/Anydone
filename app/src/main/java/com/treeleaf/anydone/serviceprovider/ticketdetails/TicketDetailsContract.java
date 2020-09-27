package com.treeleaf.anydone.serviceprovider.ticketdetails;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class TicketDetailsContract {

    public interface TicketDetailsView extends BaseView {

        void onLinkShareSuccess(String link);

        void onLinkShareFail(String msg);

    }

    public interface TicketDetailsPresenter extends Presenter<TicketDetailsView> {

        void publishSubscriberJoinEvent(String userAccountId, String accountName, String accountPicture,
                                        long ticketId);

        void publishParticipantLeftEvent(String userAccountId, String accountName, String accountPicture,
                                         long ticketId);

        void getShareLink(String ticketId);

    }
}
