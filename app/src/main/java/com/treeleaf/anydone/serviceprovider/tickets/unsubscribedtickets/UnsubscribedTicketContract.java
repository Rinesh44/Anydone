package com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class UnsubscribedTicketContract {
    public interface UnsubscribedView extends BaseView {
        void getSubscribeableTicketSuccess();

        void getSubscribeableTicketFail(String msg);

        void onSubscribeSuccess();

        void onSubscribeFail(String msg);
    }

    public interface UnsubscribedPresenter extends Presenter<UnsubscribedView> {
        void getSubscribeableTickets(long from, long to, int pageSize);

        void subscribe(long ticketId);
    }

}
