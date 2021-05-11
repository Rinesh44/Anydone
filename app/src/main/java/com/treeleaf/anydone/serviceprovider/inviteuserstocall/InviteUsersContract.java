package com.treeleaf.anydone.serviceprovider.inviteuserstocall;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;

import java.util.List;

public class InviteUsersContract {

    public interface InviteUsersView extends BaseView {

        void inviteContributorsSuccess();

        void inviteContributorsFail(String msg);

        void fetchContributorSuccess(List<AssignEmployee> contributorList);

        void fetchContributorsFail(String msg);

    }

    public interface InviteUsersPresenter extends Presenter<InviteUsersView> {

        void inviteContributors(long ticketId, List<String> contributorIds);

        void fetchContributors();
    }

}