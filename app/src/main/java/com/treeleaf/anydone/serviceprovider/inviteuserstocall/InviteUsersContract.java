package com.treeleaf.anydone.serviceprovider.inviteuserstocall;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;

import java.util.List;

public class InviteUsersContract {
    public interface InviteUsersView extends BaseView {
        void addContributorSuccess();

        void addContributorFail(String msg);

        void getContributorSuccess(List<AssignEmployee> contributorList);

        void getContributorsFail(String msg);
    }

    public interface InviteUsersPresenter extends Presenter<InviteUsersView> {
        void addContributor(long ticketId, List<String> contributorIds);

        void findContributors();
    }
}