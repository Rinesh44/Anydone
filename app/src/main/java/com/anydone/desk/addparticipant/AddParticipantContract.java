package com.anydone.desk.addparticipant;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.AssignEmployee;

import java.util.List;

public class AddParticipantContract {
    public interface AddParticipantView extends BaseView {
        void getParticipantSuccess(List<AssignEmployee> contributorList);

        void getParticipantFail(String msg);

        void addParticipantSuccess();

        void addParticipantFail(String msg);
    }

    public interface AddParticipantPresenter extends Presenter<AddParticipantContract.AddParticipantView> {
        void addParticipant(String inboxId, List<String> participantIds);

        void findParticipants(List<AssignEmployee> employeeList);
    }
}