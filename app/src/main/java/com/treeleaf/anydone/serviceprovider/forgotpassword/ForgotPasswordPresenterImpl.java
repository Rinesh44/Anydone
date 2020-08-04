package com.treeleaf.anydone.serviceprovider.forgotpassword;

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

public class ForgotPasswordPresenterImpl extends BasePresenter<ForgotPasswordContract.ForgotPasswordView>
        implements ForgotPasswordContract.ForgotPasswordPresenter {

    private static final String TAG = "ForgotPasswordPresenter";

    private ForgotPasswordRepository forgotPasswordRepository;

    @Inject
    public ForgotPasswordPresenterImpl(ForgotPasswordRepository forgotPasswordRepository) {
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    @Override
    public void sendResetCode(@NonNull String emailPhone) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(emailPhone, "EmailPhone cannot be null");

        if (!validateCredentials(emailPhone)) {
            return;
        }
        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> forgotPasswordObservable;
        try {
            forgotPasswordObservable = forgotPasswordRepository.forgotPassword(URLEncoder.encode(emailPhone, "UTF-8"));

            addSubscription(forgotPasswordObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                        @Override
                        public void onNext(UserRpcProto.UserBaseResponse forgotPasswordResponse) {
                            GlobalUtils.showLog(TAG, "forgot password response: " + forgotPasswordResponse);
                            getView().hideProgressBar();

                            if (forgotPasswordResponse == null) {
                                getView().onSendResetCodeFail("Code send failed");
                                return;
                            }

                            if (forgotPasswordResponse.getError()) {
                                getView().onSendResetCodeFail(forgotPasswordResponse.getMsg());
                                return;
                            }

                            getView().onSendResetCodeSuccess(forgotPasswordResponse.getStringValue());
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().hideProgressBar();
                            getView().onSendResetCodeFail(e.getLocalizedMessage());
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

    private boolean validateCredentials(String emailPhone) {
        if (ValidationUtils.isEmpty(emailPhone)) {
            getView().showInvalidEmailPhoneError();
            return false;
        }

        return true;
    }
}
