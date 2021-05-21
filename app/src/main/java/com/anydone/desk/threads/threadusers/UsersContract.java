package com.anydone.desk.threads.threadusers;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class UsersContract {
    public interface UsersView extends BaseView {

        void findCustomersSuccess();

        void findCustomersFail(String msg);

    }

    public interface UsersPresenter extends Presenter<UsersView> {
        void findCustomers(boolean showProgress);
    }
}
