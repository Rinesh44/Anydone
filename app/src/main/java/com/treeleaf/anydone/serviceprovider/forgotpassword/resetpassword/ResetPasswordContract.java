package com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

class ResetPasswordContract {
    public interface ResetPasswordView extends BaseView {
        void showInvalidNewPasswordError();

        void showInvalidConfirmPasswordError();

        void showPasswordNotMatchError();

        void onInvalidNewPassword();

        void onInvalidConfirmPassword();

        void onResetPasswordSuccess();

        void onResetPasswordFail(String msg);

        void showResendCode(boolean show);

        void onResendCodeFail(String msg);

        void onResendCodeSuccess();

        void startTimerCountDown();

        void cancelCountDownTimer();

        void onChangePasswordSuccess();

        void onChangePasswordFail(String msg);

        void showSamePasswordError();

        void showInvalidOldPasswordError();

        void onInvalidOldPassword();
    }

    public interface ResetPasswordPresenter extends
            Presenter<ResetPasswordContract.ResetPasswordView> {
        void resetPassword(@NonNull String emailPhone,
                           @NonNull String newPassword,
                           @NonNull String confirmPassword,
                           @NonNull String accountId,
                           @NonNull String code);

        void resendCode(String emailPhone);

        void changePassword(@NonNull String oldPassword, @NonNull String newPassword,
                            @NonNull String confirmPassword);
    }
}
