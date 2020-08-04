package com.treeleaf.anydone.serviceprovider.editprofile;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class EditProfileContract {
    public interface EditProfileView extends BaseView {
        void showInvalidFullNameError();

        void showInvalidGenderError();

        void onEditProfileSuccess();

        void onEditProfileFail(String msg);

        void onInvalidFullName();

        void onInvalidGender();

        void onPlaceAutocompleteFail(String msg);
    }

    public interface EditProfilePresenter extends Presenter<EditProfileView> {
        void editProfile(@NonNull String consumerProfileId,
                         @NonNull String accountId,
                         @NonNull String fullName,
                         @NonNull String gender);

    }
}
