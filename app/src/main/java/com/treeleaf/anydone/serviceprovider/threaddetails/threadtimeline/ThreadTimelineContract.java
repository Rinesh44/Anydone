package com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class ThreadTimelineContract {

    public interface ThreadTimelineView extends BaseView {

        void getEmployeeSuccess();

        void getEmployeeFail(String msg);

        void enableBotSuccess();

        void enableBotFail(String msg);

        void disableBotFail(String msg);

        void disableBotSuccess();

        void showProgressEmployee();

        void hideProgressEmployee();

        void assignSuccess(String empId);

        void assignFail(String msg);

        void getThreadByIdSuccess();

        void getThreadByIdFail(String msg);

        void getLinkedTicketSuccess();

        void getLinkedTicketFail(String msg);

    }

    public interface ThreadTimelinePresenter extends Presenter<ThreadTimelineView> {

        void getEmployees();

        void enableBot(String threadId);

        void disableBot(String threadId);

        void assignEmployee(String threadId, String employeeId);

        void getThreadById(String threadId);

        void getLinkedTickets(String threadId);
    }
}
