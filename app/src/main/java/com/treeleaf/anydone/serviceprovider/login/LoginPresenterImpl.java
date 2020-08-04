package com.treeleaf.anydone.serviceprovider.login;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConsumerRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.rpc.AuthRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ValidationUtils;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenterImpl extends BasePresenter<LoginContract.LoginView> implements
        LoginContract.LoginPresenter {
    private static final String TAG = "LoginPresenterImpl";
    private LoginRepository loginRepository;

    @Inject
    public LoginPresenterImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public void loginWithEmailPhone(@NonNull String emailPhone, @NonNull String password) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(emailPhone, "Username cannot be null");
        Preconditions.checkNotNull(password, "Password cannot be null");
        if (!validateCredentials(emailPhone, password)) {
            return;
        }
        getView().showProgressBar("Logging in...");
        Observable<AuthRpcProto.AuthBaseResponse> loginObservable;
        if (ValidationUtils.isNumeric(emailPhone)) {
            loginObservable = loginRepository.loginWithPhone(emailPhone, password);
        } else {
            loginObservable = loginRepository.loginWithEmail(emailPhone, password);
        }

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

                        boolean isPhone;
                        isPhone = ValidationUtils.isNumeric(emailPhone);
                        if (isPhone) {
                            Hawk.put(Constants.IS_PHONE, true);
                            Hawk.put(Constants.EMAIL_PHONE, loginResponse.getLoginResponse().
                                    getUser().getConsumer().getAccount().getPhone());
                        } else {
                            Hawk.put(Constants.IS_PHONE, false);
                            Hawk.put(Constants.EMAIL_PHONE, loginResponse.getLoginResponse().
                                    getUser().getConsumer().getAccount().getEmail());
                        }

                        Hawk.put(Constants.COUNTRY_CODE, loginResponse.getLoginResponse().
                                getUser().getConsumer().getAccount().getCountryCode());
                        Hawk.put(Constants.CURRENCY, loginResponse.getLoginResponse().
                                getUser().getConsumer().getAccount().getCurrencyCode());
                        Hawk.put(Constants.TIMEZONE_ID, loginResponse.getLoginResponse().
                                getUser().getConsumer().getAccount().getTimezone());
                        Hawk.put(Constants.SELECTED_LANGUAGE, loginResponse.getLoginResponse().
                                getUser().getConsumer().getAccount().getLanguage());

                        checkAccountStatus(loginResponse);
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

    private void checkAccountStatus(AuthRpcProto.AuthBaseResponse loginResponse) {
        switch (loginResponse.getLoginResponse().getUser().getConsumer().getAccount().getStatus()) {
            case ACCOUNT_DELETED:
                getView().onLoginFail("Your account has been deleted");
                break;

            case ACCOUNT_SUSPENDED:
                getView().onLoginFail("Your account has been suspended");
                break;

            case ACCOUNT_NOT_VERIFIED:
                getView().onAccountNotVerified();
                break;

            case ACCOUNT_VERIFIED:
                Hawk.put(Constants.TOKEN, loginResponse.getLoginResponse().getToken());
                Hawk.put(Constants.LOGGED_IN, true);

                ConsumerRepo.getInstance().saveConsumer(loginResponse.getLoginResponse(),
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "Consumer saved");
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "Failed to save consumer");

                            }
                        });

                AccountRepo.getInstance().saveAccount(loginResponse.getLoginResponse(),
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "Account saved");
                                getView().onLoginSuccess();
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "Failed to save account");
                            }
                        });

                break;

            default:
                break;
        }
    }

    @Override
    public void loginWithGoogle(@NonNull String idToken) {

    }

    private boolean validateCredentials(String emailPhone, String password) {

        if (ValidationUtils.isEmpty(emailPhone)) {
            getView().showEmptyPhoneOrEmailFieldError("Empty field!");
            return false;
        }

        if (ValidationUtils.isEmpty(password)) {
            getView().showEmptyPasswordFieldError("Empty field!");
            return false;
        }

        if (ValidationUtils.isNumeric(emailPhone)) {
            GlobalUtils.showLog(TAG, "is numeric: " + ValidationUtils.isNumeric(emailPhone));
            return true;
        }

        if (!ValidationUtils.isEmailValid(emailPhone)) {
            GlobalUtils.showLog(TAG, "is valid email: " +
                    ValidationUtils.isEmailValid(emailPhone));
            getView().showInvalidEmailPhoneError();
            return false;
        }
        return true;
    }


}
