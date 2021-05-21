package com.anydone.desk.account;


import androidx.annotation.NonNull;

import com.anydone.desk.base.presenter.BasePresenter;
import com.treeleaf.anydone.rpc.AuthRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.GlobalUtils;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class AccountPresenterImpl extends BasePresenter<AccountContract.AccountView>
        implements AccountContract.AccountPresenter {
    private static final String TAG = "AccountPresenterImpl";
    private AccountRepository accountRepository;

    @Inject
    public AccountPresenterImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void logout(@NonNull String token) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(token, "Token cannot be null");

        getView().showProgressBar("Logging out...");
        Observable<AuthRpcProto.AuthBaseResponse> logoutObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        logoutObservable = service.logout(token);

        addSubscription(logoutObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AuthRpcProto.AuthBaseResponse>() {
                    @Override
                    public void onNext(AuthRpcProto.AuthBaseResponse logoutResponse) {
                        GlobalUtils.showLog(TAG, "logout response: " + logoutResponse);

                        getView().hideProgressBar();
                        if (logoutResponse == null) {
                            getView().onLogoutFail("Logout failed");
                            return;
                        }

                        if (logoutResponse.getError()) {
                            getView().onLogoutFail(logoutResponse.getMsg());
                            return;
                        }

                        getView().onLogoutSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onLogoutFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }
}
