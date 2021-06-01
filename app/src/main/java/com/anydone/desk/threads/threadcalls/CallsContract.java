package com.anydone.desk.threads.threadcalls;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class CallsContract {

    public interface CallsView extends BaseView {


    }

    public interface CallsPresenter extends Presenter<CallsView> {
    }
}
