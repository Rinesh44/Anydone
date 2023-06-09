package com.anydone.desk.changepassword;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.UserProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ValidationUtils;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ChangePasswordPresenterImpl extends
        BasePresenter<ChangePasswordContract.ChangePasswordView>
        implements ChangePasswordContract.ChangePasswordPresenter {

    private static final String TAG = "ChangePasswordPresenter";
    private ChangePasswordRepository changePasswordRepository;

    @Inject
    public ChangePasswordPresenterImpl(ChangePasswordRepository changePasswordRepository) {
        this.changePasswordRepository = changePasswordRepository;
    }

    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword,
                               @NonNull String confirmPassword) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(newPassword, "New Password cannot be null");
        Preconditions.checkNotNull(confirmPassword, "Confirm Password cannot be null");

        if (!validateCredentials(oldPassword, newPassword, confirmPassword)) {
            return;
        }

        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<UserRpcProto.UserBaseResponse> changePasswordObservable;
        String token = Hawk.get(Constants.TOKEN);
        if (token == null) {
            getView().onChangePasswordFail("Authorization failed");
            return;
        }

        UserProto.PasswordChangeRequest passwordChangeRequest =
                UserProto.PasswordChangeRequest.newBuilder()
                        .setOldPassword(oldPassword)
                        .setNewPassword(newPassword)
                        .build();

        changePasswordObservable = service.changePassword(token, passwordChangeRequest);

        addSubscription(changePasswordObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse changePasswordResponse) {
                        GlobalUtils.showLog(TAG, "change password response:"
                                + changePasswordResponse);
                        getView().hideProgressBar();

                        if (changePasswordResponse == null) {
                            getView().onChangePasswordFail("Failed to change password");
                            return;
                        }

                        if (changePasswordResponse.getError()) {
                            getView().onChangePasswordFail(changePasswordResponse.getMsg());
                            return;
                        }

                        getView().onChangePasswordSuccess();

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


    private boolean validateCredentials(String oldPassword, String newPassword,
                                        String confirmPassword) {

        if (ValidationUtils.isEmpty(oldPassword)) {
            getView().showInvalidOldPasswordError();
            return false;
        }

        if (ValidationUtils.isEmpty(newPassword)) {
            getView().showInvalidNewPasswordError();
            return false;
        }

        if (ValidationUtils.isEmpty(confirmPassword)) {
            getView().showInvalidConfirmPasswordError();
            return false;
        }

        if (oldPassword.equals(newPassword)) {
            getView().showSamePasswordError();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            getView().showPasswordNotMatchError();
            return false;
        }

        return true;
    }
}
