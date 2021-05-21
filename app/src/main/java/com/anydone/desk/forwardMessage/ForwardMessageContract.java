package com.anydone.desk.forwardMessage;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.AssignEmployee;

import java.util.List;

public class ForwardMessageContract {
    public interface ForwardMessageView extends BaseView {

        void forwardMessageSuccess();

        void forwardMessageFail(String msg);

        void getParticipantSuccess(List<AssignEmployee> assignEmployeeList);

        void getParticipantFail(String msg);

    }

    public interface ForwardMessagePresenter extends Presenter<ForwardMessageContract.ForwardMessageView> {
        void forwardMessage(List<String> participants, String message, String inboxType);

        void findParticipants();
    }
}
