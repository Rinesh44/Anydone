package com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class VerifyCodePresenterImpl extends BasePresenter<VerifyCodeContract.VerifyCodeView>
        implements VerifyCodeContract.VerifyCodePresenter {

    private static final String TAG = "VerifyCodePresenterImpl";
    private VerifyCodeRepository verifyCodeRepository;

    @Inject
    VerifyCodePresenterImpl(VerifyCodeRepository verifyCodeRepository) {
        this.verifyCodeRepository = verifyCodeRepository;

    }

    @Override
    public void resendCode(String emailPhone) {
        getView().resetPinEditText();
        getView().showResendCode(false);
        getView().startTimerCountDown();

        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> resendCodeObservable;

        resendCodeObservable = verifyCodeRepository.resendCode(emailPhone);

        addSubscription(resendCodeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse resendCodeResponse) {
                        GlobalUtils.showLog(TAG, "resend code response: " + resendCodeResponse);
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

    }
}
