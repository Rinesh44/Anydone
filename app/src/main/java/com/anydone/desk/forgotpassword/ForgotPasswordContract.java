package com.anydone.desk.forgotpassword;

import androidx.annotation.NonNull;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

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
