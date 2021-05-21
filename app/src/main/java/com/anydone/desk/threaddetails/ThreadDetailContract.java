package com.anydone.desk.threaddetails;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class ThreadDetailContract {

    public interface ThreadDetailView extends BaseView {


    }

    public interface ThreadDetailsPresenter extends Presenter<ThreadDetailView> {

    }
}
