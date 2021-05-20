package com.treeleaf.anydone.serviceprovider.threads.threadusers;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class UsersContract {
    public interface UsersView extends BaseView {

        void findCustomersSuccess();

        void findCustomersFail(String msg);

    }

    public interface UsersPresenter extends Presenter<UsersView> {
        void findCustomers(boolean showProgress);
    }
}
