package com.anydone.desk.inboxdetails;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class InboxDetailContract {
    public interface InboxDetailView extends BaseView {


    }

    public interface InboxDetailsPresenter extends Presenter<InboxDetailContract.InboxDetailView> {

    }
}
