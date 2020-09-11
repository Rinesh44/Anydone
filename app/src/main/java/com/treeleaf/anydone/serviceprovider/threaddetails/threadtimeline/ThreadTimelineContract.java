package com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;

public class ThreadTimelineContract {

    public interface ThreadTimelineView extends BaseView {

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

        void enableBotSuccess();

        void enableBotFail(String msg);

        void disableBotFail(String msg);

        void disableBotSuccess();

    }

    public interface ThreadTimelinePresenter extends Presenter<ThreadTimelineView> {

        void getEmployees();

        void enableBot(String threadId);

        void disableBot(String threadId);
    }
}
