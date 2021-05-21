package com.anydone.desk.account;


import androidx.annotation.NonNull;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class AccountContract {

    public interface AccountView extends BaseView {
        void onLogoutSuccess();

        void onLogoutFail(String msg);
    }

    public interface AccountPresenter extends Presenter<AccountView> {
        void logout(@NonNull String token);
    }

}
