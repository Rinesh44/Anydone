package com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.rpc.UserRpcProto;
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
        Observable<UserRpcProto.UserBaseResponse> resetPasswordObservable;
        resetPasswordObservable = resetPasswordRepository.resetPassword(emailPhone,
                newPassword, confirmPassword, accountId, code);

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
        Observable<UserRpcProto.UserBaseResponse> resendCodeObservable;

        try {
            resendCodeObservable = resetPasswordRepository
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
        }

        return true;
    }

}
