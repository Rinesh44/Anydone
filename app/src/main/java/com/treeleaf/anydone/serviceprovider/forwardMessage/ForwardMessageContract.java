package com.treeleaf.anydone.serviceprovider.forwardMessage;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;

import java.util.List;

public class ForwardMessageContract {
    public interface ForwardMessageView extends BaseView {

        void forwardMessageSuccess();

        void forwardMessageFail(String msg);

        void getParticipantSuccess(List<AssignEmployee> assignEmployeeList);

        void getParticipantFail(String msg);

    }

    public interface ForwardMessagePresenter extends Presenter<ForwardMessageContract.ForwardMessageView> {
        void forwardMessage(List<String> participants, String message);

        void findParticipants();
    }
}
