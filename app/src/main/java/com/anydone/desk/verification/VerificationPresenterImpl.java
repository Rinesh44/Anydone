package com.anydone.desk.verification;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.AuthRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.ServiceProviderRepo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ValidationUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class VerificationPresenterImpl extends BasePresenter<VerificationContract.VerificationView>
        implements VerificationContract.VerificationPresenter {
    private static final String TAG = "VerificationPresenterIm";
    private VerificationRepository verificationRepository;

    @Inject
    public VerificationPresenterImpl(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    @Override
    public void resendCode() {
        getView().resetAllEditTexts();
        getView().showResendCode(false);
        getView().startTimerCountDown();

        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<UserRpcProto.UserBaseResponse> resendCodeObservable;

        try {
            resendCodeObservable = service
                    .resendCode(URLEncoder.encode(Hawk.get(Constants.EMAIL_PHONE), "UTF-8"));

            addSubscription(resendCodeObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                        @Override
                        public void onNext(UserRpcProto.UserBaseResponse resendCodeResponse) {
                            GlobalUtils.showLog(TAG, "resend code response: "
                                    + resendCodeResponse);
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

    @Override
    public void verify(@NonNull String digits) {

        Preconditions.checkNotNull(getView(), "View is not attached");

        if (!validateCredentials(digits)) {
            return;
        }

        getView().showProgressBar("Verifying user...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<UserRpcProto.UserBaseResponse> verifyObservable;
        String emailPhone = Hawk.get(Constants.EMAIL_PHONE);

        UserProto.UserVerification userVerification = UserProto.UserVerification.newBuilder()
                .setEmailPhone(Hawk.get(Constants.EMAIL_PHONE, ""))
                .setCode(Integer.parseInt(digits))
                .build();
        if (ValidationUtils.isNumeric(emailPhone.replace("+", ""))) {
            verifyObservable = service.verifyCodeWithPhone(userVerification);
        } else {
            verifyObservable = service.verifyCodeWithEmail(userVerification);
        }

        addSubscription(verifyObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse verifyResponse) {
                        GlobalUtils.showLog(TAG, "verify response: " + verifyResponse);
                        if (verifyResponse == null) {
                            getView().hideProgressBar();
                            getView().onVerificationFail("Verification failed");
                            return;
                        }

                        if (verifyResponse.getError()) {
                            getView().hideProgressBar();
                            getView().onVerificationFail(verifyResponse.getMsg());
                            return;
                        }

                        Hawk.put(Constants.USER_VERIFIED, true);
                        getView().onVerificationSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onVerificationFail(e.getLocalizedMessage());
                        getView().showResendCode(true);
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    @Override
    public void login(@NonNull String emailPhone, @NonNull String password) {
        getView().hideProgressBar();
        getView().showProgressBar("Logging in...");
        Observable<AuthRpcProto.AuthBaseResponse> loginObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        AuthProto.LoginRequest loginRequest = AuthProto.LoginRequest.newBuilder()
                .setEmailPhone(emailPhone)
                .setPassword(password)
                .build();

        loginObservable = service.login(loginRequest);

        addSubscription(loginObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AuthRpcProto.AuthBaseResponse>() {
                    @Override
                    public void onNext(AuthRpcProto.AuthBaseResponse loginResponse) {
                        GlobalUtils.showLog(TAG, "login response: " + loginResponse);

                        getView().hideProgressBar();
                        if (loginResponse == null) {
                            getView().onLoginFail("Login failed");
                            return;
                        }

                        if (loginResponse.getError()) {
                            getView().onLoginFail(loginResponse.getMsg());
                            return;
                        }
                        saveLoginData(emailPhone, loginResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onLoginFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );

    }

    private void saveLoginData(String emailPhone, AuthRpcProto.AuthBaseResponse loginResponse) {
        boolean isPhone;
        GlobalUtils.showLog(TAG, "check email phone:" + emailPhone);
        if (ValidationUtils.isEmailValid(emailPhone)) {
            isPhone = false;
        } else {
            isPhone = ValidationUtils.isNumeric(emailPhone.replaceAll("[^0-9]",
                    ""));
        }

        if (isPhone) {
            Hawk.put(Constants.IS_PHONE, true);
            Hawk.put(Constants.EMAIL_PHONE, loginResponse.getLoginResponse()
                    .getUser().getConsumer().getAccount().getPhone());
        } else {
            Hawk.put(Constants.IS_PHONE, false);
            Hawk.put(Constants.EMAIL_PHONE, loginResponse.getLoginResponse()
                    .getUser().getConsumer().getAccount().getEmail());
        }
        Hawk.put(Constants.COUNTRY_CODE, loginResponse.getLoginResponse()
                .getUser().getConsumer().getAccount().getCountryCode());

        Hawk.put(Constants.TOKEN, loginResponse.getLoginResponse().getToken());
        Hawk.put(Constants.LOGGED_IN, true);

        ServiceProviderRepo.getInstance().saveServiceProvider(loginResponse.getLoginResponse(),
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        GlobalUtils.showLog(TAG, "Service provider saved");
                    }

                    @Override
                    public void fail() {
                        GlobalUtils.showLog(TAG, "Failed to save service provider");

                    }
                });

        AccountRepo.getInstance().saveAccount(loginResponse.getLoginResponse(), false,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        GlobalUtils.showLog(TAG, "Service provider Account saved");
                        getView().onLoginSuccess();
                    }

                    @Override
                    public void fail() {
                        GlobalUtils.showLog(TAG, "Failed to save service provider account");
                    }
                });
    }


    private boolean validateCredentials(String digits) {
        if (ValidationUtils.isEmpty(digits)) {
            getView().showEmptyFieldError("Invalid code");
            return false;
        }

        return true;
    }


}
