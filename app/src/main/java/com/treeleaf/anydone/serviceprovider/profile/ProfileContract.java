package com.treeleaf.anydone.serviceprovider.profile;


import android.net.Uri;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class ProfileContract {

    public interface ProfileView extends BaseView {
        void onUploadImageSuccess();

        void onUploadImageFail(String msg);

        void onResendCodeSuccess();

        void onResendCodeFail(String msg);

        void onDataChange(String msg);

        void showInvalidPhoneError();

        void showInvalidEmailError();

        void onAddEmailFail(String msg);

        void onAddPhoneFail(String msg);

        void onAddEmailSuccess();

        void onAddPhoneSuccess();
    }

    public interface ProfilePresenter extends Presenter<ProfileView> {
        void uploadImage(Uri uri);

        void resendCode(String emailPhone);

        void addPhone(String phone);

        void addEmail(String email);

        void setPhoneVerified();

        void setEmailVerified();
    }

}
