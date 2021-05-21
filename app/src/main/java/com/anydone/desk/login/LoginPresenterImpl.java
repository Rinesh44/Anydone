package com.anydone.desk.login;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.rpc.AuthRpcProto;
import com.anydone.desk.AnyDoneServiceProviderApplication;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.EmployeeRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.ServiceProviderRepo;
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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        Observable<AuthRpcProto.AuthBaseResponse> loginObservable;
        String pushToken = AnyDoneServiceProviderApplication.getFirebaseToken();
        GlobalUtils.showLog(TAG, "firebase token: " + pushToken);

        if (pushToken == null) pushToken = "pushToken";
        AuthProto.LoginRequest loginRequest = AuthProto.LoginRequest.newBuilder()
                .setEmailPhone(emailPhone)
                .setPassword(password)
                .setPushToken(pushToken)
                .build();

        GlobalUtils.showLog(TAG, "login info: " + loginRequest);
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

                        checkLoginType(loginResponse, password);
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

    private void checkLoginType(AuthRpcProto.AuthBaseResponse loginResponse, String password) {
        //check for consumer login case
        if (!loginResponse.getLoginResponse().getUser().getConsumer().getAccount()
                .getAccountId().isEmpty()) {
            getView().onLoginFail("Please login from consumer app");
            return;
        }

        //check for employee login case
        if (!loginResponse.getLoginResponse().getUser().getEmployee().getAccount()
                .getAccountId().isEmpty()) {

            if (loginResponse.getLoginResponse().getUser().getEmployee().getPasswordChanged()) {
                EmployeeRepo.getInstance().saveEmployee(loginResponse.getLoginResponse(),
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "employee saved");
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to save employee");
                            }
                        });

                AccountRepo.getInstance().saveAccount(loginResponse.getLoginResponse(), true,
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "employee account saved");
                                Hawk.put(Constants.TOKEN, loginResponse.getLoginResponse().getToken());
                                Hawk.put(Constants.SESSION_ID, loginResponse.getLoginResponse().getSessionId());
                                Hawk.put(Constants.LOGGED_IN, true);
                                getView().onLoginSuccess();
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to save employee account");
                            }
                        });
            } else {
                Hawk.put(Constants.TOKEN, loginResponse.getLoginResponse().getToken());
                Hawk.put(Constants.SESSION_ID, loginResponse.getLoginResponse().getSessionId());
                getView().onEmployeeFirstLogin(password);
            }

            return;
        }

        //check for service provider login case
        if (!loginResponse.getLoginResponse().getUser().getServiceProvider()
                .getAccount().getAccountId().isEmpty()) {
            checkAccountStatus(loginResponse);
        }

    }

    private void checkAccountStatus(AuthRpcProto.AuthBaseResponse loginResponse) {
        switch (loginResponse.getLoginResponse().getUser().getServiceProvider().getAccount().getStatus()) {
            case ACCOUNT_DEACTIVATED:
                getView().onLoginFail("Your account has been deleted");
                break;

            case ACCOUNT_SUSPENDED:
                getView().onLoginFail("Your account has been suspended");
                break;

            case ACCOUNT_NOT_VERIFIED:
                getView().onAccountNotVerified();
                break;

            case ACCOUNT_VERIFIED:
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
                                Hawk.put(Constants.TOKEN, loginResponse.getLoginResponse().getToken());
                                Hawk.put(Constants.SESSION_ID, loginResponse.getLoginResponse().getSessionId());
                                Hawk.put(Constants.LOGGED_IN, true);
                                getView().onLoginSuccess();
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "Failed to save service provider account");
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
            getView().showInvalidEmailPhoneError();
            return false;
        }

        if (ValidationUtils.isEmpty(password)) {
            getView().showInvalidPasswordError();
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
