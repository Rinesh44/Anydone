package com.treeleaf.anydone.serviceprovider.addparticipant;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;

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