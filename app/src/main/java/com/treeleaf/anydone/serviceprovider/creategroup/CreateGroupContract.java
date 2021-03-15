package com.treeleaf.anydone.serviceprovider.creategroup;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;

import java.util.List;

public class CreateGroupContract {
    public interface CreateGroupView extends BaseView {

        void createGroupSuccess();

        void createGroupFail(String msg);

        void getParticipantSuccess(List<AssignEmployee> assignEmployeeList);

        void getParticipantFail(String msg);

    }

    public interface CreateGroupPresenter extends Presenter<CreateGroupContract.CreateGroupView> {
        void createGroup(List<String> participants, String message, String subject,  boolean isGroup);

        void findParticipants();
    }
}
