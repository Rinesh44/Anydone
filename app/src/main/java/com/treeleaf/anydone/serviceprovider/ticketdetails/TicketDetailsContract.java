package com.treeleaf.anydone.serviceprovider.ticketdetails;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class TicketDetailsContract {

    public interface TicketDetailsView extends BaseView {


    }

    public interface TicketDetailsPresenter extends Presenter<TicketDetailsView> {

    }
}
