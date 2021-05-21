package com.anydone.desk.inviteuserstocall;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.AssignEmployee;

import java.util.ArrayList;
import java.util.List;

public class InviteUsersContract {

    public interface InviteUsersView extends BaseView {

        void fetchContributorSuccess(List<AssignEmployee> contributorList);

        void fetchContributorsFail(String msg);

    }

    public interface InviteUsersPresenter extends Presenter<InviteUsersView> {

        void fetchContributors();

        ArrayList<AddedParticipantsForCall> transform(List<AssignEmployee> assignEmployees);

    }

}