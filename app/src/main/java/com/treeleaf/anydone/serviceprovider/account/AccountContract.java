package com.treeleaf.anydone.serviceprovider.account;


import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class AccountContract {

    public interface AccountView extends BaseView {
        void onLogoutSuccess();

        void onLogoutFail(String msg);
    }

    public interface AccountPresenter extends Presenter<AccountView> {
        void logout(@NonNull String token);
    }

}
