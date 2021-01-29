package com.treeleaf.anydone.serviceprovider.inboxdetails;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class InboxDetailContract {
    public interface InboxDetailView extends BaseView {


    }

    public interface InboxDetailsPresenter extends Presenter<InboxDetailContract.InboxDetailView> {

    }
}
