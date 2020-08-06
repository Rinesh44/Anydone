package com.treeleaf.anydone.serviceprovider.editprofile;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ValidationUtils;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class EditProfilePresenterImpl extends BasePresenter<EditProfileContract.EditProfileView>
        implements EditProfileContract.EditProfilePresenter {
    private static final String TAG = "EditProfilePresenterImp";
    private EditProfileRepository editProfileRepository;

    @Inject
    public EditProfilePresenterImpl(EditProfileRepository editProfileRepository) {
        this.editProfileRepository = editProfileRepository;
    }

    @Override
    public void editEmployeeProfile(@NonNull String employeeProfileId,
                                    @NonNull String accountId,
                                    @NonNull String fullName,
                                    @NonNull String gender) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(fullName, "FullName cannot be null");
//        Preconditions.checkNotNull(address, "Address cannot be null");
        Preconditions.checkNotNull(employeeProfileId,
                "Employee profile id cannot be null");
        Preconditions.checkNotNull(accountId, "Account id cannot be null");
        Preconditions.checkNotNull(gender, "Gender cannot be null");

        if (!validateCredentials(fullName, gender)) {
            return;
        }

        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> editProfileObservable;
        GlobalUtils.showLog(TAG, "gender: " + gender);
        AnydoneProto.Gender selectedGender = GlobalUtils.getGender(gender);

        UserProto.EmployeeProfile employeeProfile = setEmployeeDataToEntity(employeeProfileId, accountId,
                fullName, selectedGender);
        editProfileObservable = editProfileRepository.editEmployeeProfile(employeeProfile);

        addSubscription(editProfileObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse editProfileResponse) {
                        GlobalUtils.showLog(TAG, "edit profile response: " +
                                editProfileResponse);

                        getView().hideProgressBar();
                        if (editProfileResponse == null) {
                            getView().onEditProfileFail("Edit profile failed");
                            return;
                        }

                        if (editProfileResponse.getError()) {
                            getView().onEditProfileFail(editProfileResponse.getMsg());
                            return;
                        }

                        AccountRepo.getInstance().editEmployeeAccount(accountId,
                                editProfileResponse.getEmployee(), new Repo.Callback() {
                                    @Override
                                    public void success(Object o) {
                                        GlobalUtils.showLog(TAG, "account edited");
                                        getView().onEditProfileSuccess();
                                    }

                                    @Override
                                    public void fail() {
                                        GlobalUtils.showLog(TAG,
                                                "account edit failed");
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));
    }

    @Override
    public void editServiceProviderProfile(@NonNull String serviceProviderProfileId,
                                           @NonNull String accountId,
                                           @NonNull String fullName,
                                           @NonNull String gender) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(fullName, "FullName cannot be null");
//        Preconditions.checkNotNull(address, "Address cannot be null");
        Preconditions.checkNotNull(serviceProviderProfileId,
                "Service provider profile id cannot be null");
        Preconditions.checkNotNull(accountId, "Account id cannot be null");
        Preconditions.checkNotNull(gender, "Gender cannot be null");

        if (!validateCredentials(fullName, gender)) {
            return;
        }

        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> editProfileObservable;
        GlobalUtils.showLog(TAG, "gender: " + gender);
        AnydoneProto.Gender selectedGender = GlobalUtils.getGender(gender);

        UserProto.ServiceProviderProfile serviceProviderProfile =
                setServiceProviderDataToEntity(serviceProviderProfileId, accountId,
                        fullName, selectedGender);
        editProfileObservable = editProfileRepository.editServiceProviderProfile(serviceProviderProfile);

        addSubscription(editProfileObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse editProfileResponse) {
                        GlobalUtils.showLog(TAG, "edit profile response: " +
                                editProfileResponse);

                        getView().hideProgressBar();
                        if (editProfileResponse == null) {
                            getView().onEditProfileFail("Edit profile failed");
                            return;
                        }

                        if (editProfileResponse.getError()) {
                            getView().onEditProfileFail(editProfileResponse.getMsg());
                            return;
                        }

                        AccountRepo.getInstance().editServiceProviderAccount(accountId,
                                editProfileResponse.getServiceProvider(), new Repo.Callback() {
                                    @Override
                                    public void success(Object o) {
                                        GlobalUtils.showLog(TAG, "account edited");
                                        getView().onEditProfileSuccess();
                                    }

                                    @Override
                                    public void fail() {
                                        GlobalUtils.showLog(TAG,
                                                "account edit failed");
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));

    }

    private boolean validateCredentials(String fullName, String gender) {

        if (ValidationUtils.isEmpty(fullName)) {
            getView().showInvalidFullNameError();
            return false;
        }

        if (gender == null) {
            getView().showInvalidGenderError();
            return false;
        }

        return true;
    }

    private UserProto.EmployeeProfile setEmployeeDataToEntity(@NonNull String employeeProfileId,
                                                              @NonNull String accountId,
                                                              @NonNull String fullName,
                                                              @NonNull AnydoneProto.Gender gender) {

        UserProto.Account account = UserProto.Account.newBuilder()
                .setFullName(fullName)
                .setAccountId(accountId)
                .setCountryCode(Hawk.get(Constants.COUNTRY_CODE))
                .build();

        UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                .setEmployeeProfileId(employeeProfileId)
                .setGender(gender)
                .setAccount(account)
                .setUpdatedAt(System.currentTimeMillis())
                .build();

        GlobalUtils.showLog(TAG, "edit profile check: " + employeeProfile);

        return employeeProfile;
    }

    private UserProto.ServiceProviderProfile setServiceProviderDataToEntity(@NonNull String serviceProviderProfileId,
                                                                            @NonNull String accountId,
                                                                            @NonNull String fullName,
                                                                            @NonNull AnydoneProto.Gender gender) {

        UserProto.Account account = UserProto.Account.newBuilder()
                .setFullName(fullName)
                .setAccountId(accountId)
                .setCountryCode(Hawk.get(Constants.COUNTRY_CODE))
                .build();

        UserProto.ServiceProviderProfile serviceProviderProfile = UserProto.ServiceProviderProfile.newBuilder()
                .setServiceProviderProfileId(serviceProviderProfileId)
//                .setGender(gender)
                .setAccount(account)
                .setUpdatedAt(System.currentTimeMillis())
                .build();

        GlobalUtils.showLog(TAG, "edit profile check: " + serviceProviderProfile);

        return serviceProviderProfile;
    }
}
