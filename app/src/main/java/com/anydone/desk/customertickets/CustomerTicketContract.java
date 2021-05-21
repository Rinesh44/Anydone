package com.anydone.desk.customertickets;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class CustomerTicketContract {
    public interface CustomerTicketView extends BaseView {

        void getCustomerTicketSuccess();

        void getCustomerTicketFail(String msg);
    }

    public interface CustomerTicketPresenter extends Presenter<CustomerTicketContract.CustomerTicketView> {

        void getCustomerTickets(String customerId, long from, long to, int page);

    }
}
