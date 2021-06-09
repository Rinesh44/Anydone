package com.anydone.desk.threaddetails.threadtimeline;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.ConversationThreadLabel;

import java.util.List;

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

        void setImportantSuccess(boolean value);

        void setImportantFail(String msg);

        void onFollowUpSuccess(boolean value);

        void onFollowUpFail(String msg);

        void setFollowUpDateSuccess(long date);

        void setFollowUpDateFail(String msg);

        void convertToUserSuccess();

        void convertToUserFail(String msg);

        void getConversationLabelSuccess();

        void getConversationLabelFail(String msg);

        void addConversationLabelSuccess();

        void addConversationLabelFail(String msg);

    }

    public interface ThreadTimelinePresenter extends Presenter<ThreadTimelineView> {
        void getEmployees();

        void enableBot(String threadId);

        void disableBot(String threadId);

        void assignEmployee(String threadId, String employeeId);

        void getThreadById(String threadId);

        void getLinkedTickets(String threadId);

        void setAsImportant(String threadId, boolean isImportant);

        void followUp(String threadId, boolean followUp);

        void setFollowUpDate(String threadId, long followUpDate);

        void convertToUser(String threadId);

        void getConversationLabels(String threadId);

        void addConversationLabels(String threadId, List<ConversationThreadLabel> labels);
    }
}
