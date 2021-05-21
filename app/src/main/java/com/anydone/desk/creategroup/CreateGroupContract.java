package com.anydone.desk.creategroup;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Inbox;

import java.util.List;

public class CreateGroupContract {
    public interface CreateGroupView extends BaseView {

        void createGroupSuccess();

        void createGroupFail(String msg);

        void getParticipantSuccess(List<AssignEmployee> assignEmployeeList);

        void getParticipantFail(String msg);

        void getSubjectSuccess(List<Inbox> subjectResults);

        void getSubjectFail(String msg);
    }

    public interface CreateGroupPresenter extends Presenter<CreateGroupContract.CreateGroupView> {
        void createGroup(List<String> participants, String message, String subject, boolean isGroup,
                         boolean isPrivate);

        void findParticipants();

        void searchSubjects(String text);
    }
}
