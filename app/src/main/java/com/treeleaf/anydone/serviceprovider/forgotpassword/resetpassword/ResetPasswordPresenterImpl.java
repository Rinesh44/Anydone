package com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ValidationUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ResetPasswordPresenterImpl extends
        BasePresenter<ResetPasswordContract.ResetPasswordView>
        implements ResetPasswordContract.ResetPasswordPresenter {
    private static final String TAG = "ResetPasswordPresenterI";
    private ResetPasswordRepository resetPasswordRepository;

    @Inject
    public ResetPasswordPresenterImpl(ResetPasswordRepository resetPasswordRepository) {
        this.resetPasswordRepository = resetPasswordRepository;
    }

    @Override
    public void resetPassword(@NonNull String emailPhone,
                              @NonNull String newPassword,
                              @NonNull String confirmPassword,
                              @NonNull String accountId,
                              @NonNull String code) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(emailPhone, "EmailPhone cannot be null");
        Preconditions.checkNotNull(newPassword, "Password cannot be null");
        Preconditions.checkNotNull(confirmPassword, "Password cannot be null");
        Preconditions.checkNotNull(accountId, "AccountId cannot be null");
        Preconditions.checkNotNull(code, "Code cannot be null");

        if (!validateCredentials(emailPhone, newPassword, confirmPassword, accountId, code)) {
            return;
        }

        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<UserRpcProto.UserBaseResponse> resetPasswordObservable;

        UserProto.PasswordReset passwordReset = UserProto.PasswordReset.newBuilder()
                .setAccountId(accountId)
                .setCode(Integer.parseInt(code))
                .setEmailPhone(emailPhone)
                .setNewPassword(confirmPassword)
                .build();

        GlobalUtils.showLog(TAG, "Reset password check: " + passwordReset);

        resetPasswordObservable = service.resetPassword(passwordReset);

        addSubscription(resetPasswordObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse resetPasswordResponse) {
                        GlobalUtils.showLog(TAG, "reset password response: "
                                + resetPasswordResponse);

                        if (resetPasswordResponse == null) {
                            getView().hideProgressBar();
                            getView().onResetPasswordFail("Password reset failed");
                            return;
                        }

                        if (resetPasswordResponse.getError()) {
                            getView().hideProgressBar();
                            getView().onResetPasswordFail(resetPasswordResponse.getMsg());
                            return;
                        }

                        getView().onResetPasswordSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onResetPasswordFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );

    }

    @Override
    public void resendCode(String emailPhone) {
        getView().showResendCode(false);
        getView().startTimerCountDown();

        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<UserRpcProto.UserBaseResponse> resendCodeObservable;

        try {
            resendCodeObservable = service
                    .resendCode(URLEncoder.encode(emailPhone, "UTF-8"));

            addSubscription(resendCodeObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                        @Override
                        public void onNext(UserRpcProto.UserBaseResponse resendCodeResponse) {
                            GlobalUtils.showLog(TAG, "resend code response: " +
                                    resendCodeResponse);
                            if (resendCodeResponse == null) {
                                getView().hideProgressBar();
                                getView().onResendCodeFail("Resend code failed");
                                return;
                            }

                            if (resendCodeResponse.getError()) {
                                getView().hideProgressBar();
                                getView().onResendCodeFail(resendCodeResponse.getMsg());
                                return;
                            }

                            getView().onResendCodeSuccess();
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().hideProgressBar();
                            getView().onResendCodeFail(e.getLocalizedMessage());
                            getView().showResendCode(true);
                        }

                        @Override
                        public void onComplete() {
                            getView().hideProgressBar();
                        }
                    })
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private boolean validateCredentials(String emailPhone,
                                        String newPassword,
                                        String confirmPassword,
                                        String accountId,
                                        String code) {
        if (ValidationUtils.isEmpty(newPassword)) {
            getView().showInvalidNewPasswordError();
            return false;
        }

        if (ValidationUtils.isEmpty(confirmPassword)) {
            getView().showInvalidConfirmPasswordError();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            getView().showPasswordNotMatchError();
            return false;
        }

        if (ValidationUtils.isEmpty(emailPhone)) {
            getView().showToastMessage("Invalid emailPhone");
        }

        if (ValidationUtils.isEmpty(accountId)) {
            getView().showToastMessage("Invalid accountId");
        }

        if (ValidationUtils.isEmpty(code)) {
            getView().showToastMessage("Invalid code");
            return false;
        }

        return true;
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
