package com.anydone.desk.forgotpassword.verifyCode;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class VerifyCodeContract {

    public interface VerifyCodeView extends BaseView {

        void resetPinEditText();

        void showResendCode(boolean show);

        void onResendCodeFail(String msg);

        void onResendCodeSuccess();

        void startTimerCountDown();

        void cancelCountDownTimer();

    }

    public interface VerifyCodePresenter extends Presenter<VerifyCodeContract.VerifyCodeView> {

        void resendCode(String emailPhone);

    }

}
