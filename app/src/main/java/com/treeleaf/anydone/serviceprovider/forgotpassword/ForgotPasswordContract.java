package com.treeleaf.anydone.serviceprovider.forgotpassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class ForgotPasswordContract {
    public interface ForgotPasswordView extends BaseView {
        void showInvalidEmailPhoneError();

        void onSendResetCodeSuccess(String accountId);

        void onSendResetCodeFail(String msg);
    }

    public interface ForgotPasswordPresenter extends Presenter<ForgotPasswordView> {
        void sendResetCode(@NonNull String emailPhone);
    }

}
