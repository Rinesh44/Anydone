package com.anydone.desk.setting.timezone;

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

public class TimezonePresenterImpl extends BasePresenter<TimezoneContract.TimezoneView> implements
        TimezoneContract.TimezonePresenter {
    private static final String TAG = "TimezonePresenterImpl";
    private TimezoneRepository timezoneRepository;

    @Inject
    public TimezonePresenterImpl(TimezoneRepository timezoneRepository) {
        this.timezoneRepository = timezoneRepository;
    }

    @Override
    public void addTimezone(String token, String timezone) throws UnsupportedEncodingException {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(timezone, "timezone cannot be null");

        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> timezoneObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        timezoneObservable = service.addTimezone(token, URLEncoder.encode(timezone,
                "UTF-8"));

        addSubscription(timezoneObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse userBaseResponse) {
                        getView().hideProgressBar();
                        GlobalUtils.showLog(TAG, "add timezone response: " + userBaseResponse);
                        if (userBaseResponse == null) {
                            getView().onTimezoneAddFail("Failed to add timezone");
                            return;
                        }

                        if (userBaseResponse.getError()) {
                            getView().onTimezoneAddFail(userBaseResponse.getMsg());
                            return;
                        }

                        getView().onTimezoneAddSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onTimezoneAddFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));
    }
}