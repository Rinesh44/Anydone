package com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

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
