package com.treeleaf.anydone.serviceprovider.addcontributor;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;

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