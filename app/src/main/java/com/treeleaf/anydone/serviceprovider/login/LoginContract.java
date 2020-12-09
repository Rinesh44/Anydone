package com.treeleaf.anydone.serviceprovider.login;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class LoginContract {

    public interface LoginView extends BaseView {

        void showInvalidEmailPhoneError();

        void showInvalidPasswordError();

        void onAccountNotVerified();

        void onLoginSuccess();

        void onLoginFail(String msg);

        void onEmployeeFirstLogin(String oldPassword);

    }

    public interface LoginPresenter extends Presenter<LoginView> {

        void loginWithEmailPhone(@NonNull String emailPhone, @NonNull String password);

        void loginWithGoogle(@NonNull String idToken);

    }

}
