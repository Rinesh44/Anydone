package com.anydone.desk.setting.currency;

import com.anydone.desk.base.presenter.BasePresenter;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.GlobalUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CurrencyPresenterImpl extends BasePresenter<CurrencyContract.CurrencyView> implements
        CurrencyContract.CurrencyPresenter {
    private static final String TAG = "CurrencyPresenterImpl";
    private CurrencyRepository currencyRepository;

    @Inject
    public CurrencyPresenterImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void addCurrency(String token, String currency) throws UnsupportedEncodingException {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(currency, "currency cannot be null");

        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> currencyObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        currencyObservable = service.addCurrency(token,
                URLEncoder.encode(currency, "UTF-8"));

        addSubscription(currencyObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse userBaseResponse) {
                        getView().hideProgressBar();
                        GlobalUtils.showLog(TAG, "add currency response: " + userBaseResponse);
                        if (userBaseResponse == null) {
                            getView().onAddCurrencyFail("Failed to add currency");
                            return;
                        }

                        if (userBaseResponse.getError()) {
                            getView().onAddCurrencyFail(userBaseResponse.getMsg());
                            return;
                        }

                        getView().onAddCurrencySuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onAddCurrencyFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));
    }
}
