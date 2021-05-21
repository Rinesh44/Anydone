package com.anydone.desk.verification;

import androidx.annotation.NonNull;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class VerificationContract {

    public interface VerificationView extends BaseView {

        void onVerificationSuccess();

        void onVerificationFail(String msg);

        void onResendCodeSuccess();

        void onResendCodeFail(String msg);

        void showEmptyFieldError(String msg);

        void resetAllEditTexts();

        void showResendCode(boolean show);

        void startTimerCountDown();

        void cancelCountDownTimer();

        void onLoginFail(String msg);

        void onLoginSuccess();

    }

    public interface VerificationPresenter extends Presenter<VerificationView> {

        void verify(@NonNull String digits);

        void resendCode();

        void login(@NonNull String emailPhone, @NonNull String password);

    }

}
