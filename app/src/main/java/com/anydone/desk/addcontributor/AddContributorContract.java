package com.anydone.desk.addcontributor;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.AssignEmployee;

import java.util.List;

public class AddContributorContract {
    public interface AddContributorView extends BaseView {
        void addContributorSuccess();

        void addContributorFail(String msg);

        void getContributorSuccess(List<AssignEmployee> contributorList);

        void getContributorsFail(String msg);
    }

    public interface AddContributorPresenter extends Presenter<AddContributorView> {
        void addContributor(long ticketId, List<String> contributorIds);

        void findContributors();
    }
}