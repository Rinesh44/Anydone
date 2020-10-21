package com.treeleaf.anydone.serviceprovider.subscribed;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class SubscribedTicketContract {
    public interface SubscribeTicketsView extends BaseView {
        void getSubscribedTicketsSuccess();

        void getSubscribedTicketsFail(String msg);

        void onUnsubscribeSuccess(long ticketId);

        void onUnsubscribeFail(String msg);
    }

    public interface SubscribeTicketsPresenter extends Presenter<SubscribeTicketsView> {
        void getSubscribedTickets(boolean showProgress, long from, long to, int page);

        void unsubscribeTicket(long ticketId);
    }
}
