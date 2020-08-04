package com.treeleaf.anydone.serviceprovider.changepassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class ChangePasswordContract {
    public interface ChangePasswordView extends BaseView {
        void showInvalidOldPasswordError();

        void showInvalidNewPasswordError();

        void showInvalidConfirmPasswordError();

        void showSamePasswordError();

        void showPasswordNotMatchError();

        void onInvalidOldPassword();

        void onInvalidNewPassword();

        void onInvalidConfirmPassword();

        void onChangePasswordSuccess();

        void onChangePasswordFail(String msg);


    }

    public interface ChangePasswordPresenter extends Presenter<ChangePasswordView> {
        void changePassword(@NonNull String oldPassword, @NonNull String newPassword,
                            @NonNull String confirmPassword);
    }
}
