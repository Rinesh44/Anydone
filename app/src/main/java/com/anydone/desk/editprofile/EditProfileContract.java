package com.anydone.desk.editprofile;

import androidx.annotation.NonNull;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

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
        void editEmployeeProfile(@NonNull String employeeProfileId,
                                 @NonNull String accountId,
                                 @NonNull String fullName,
                                 @NonNull String gender);

        void editServiceProviderProfile(@NonNull String serviceProviderProfileId,
                                        @NonNull String accountId,
                                        @NonNull String fullName,
                                        @NonNull String gender);

    }
}
