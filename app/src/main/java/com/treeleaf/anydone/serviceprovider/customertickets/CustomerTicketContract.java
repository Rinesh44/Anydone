package com.treeleaf.anydone.serviceprovider.customertickets;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class CustomerTicketContract {
    public interface CustomerTicketView extends BaseView {

        void getCustomerTicketSuccess();

        void getCustomerTicketFail(String msg);
    }

    public interface CustomerTicketPresenter extends Presenter<CustomerTicketContract.CustomerTicketView> {

        void getCustomerTickets(String customerId, long from, long to, int page);

    }
}
