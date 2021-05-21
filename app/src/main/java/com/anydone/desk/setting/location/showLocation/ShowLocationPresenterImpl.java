package com.anydone.desk.setting.location.showLocation;

import com.orhanobut.hawk.Hawk;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

public class ShowLocationPresenterImpl extends BasePresenter<ShowLocationContract.ShowLocationView>
        implements ShowLocationContract.ShowLocationPresenter {
    private static final String TAG = "ShowLocationPresenterIm";

    private ShowLocationRepository repository;

    @Inject
    public ShowLocationPresenterImpl(ShowLocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void makeLocationDefault(String locationId) {
        getView().showProgressBar("Please wait...");
        String defaultLocationUrl = getDefaultLocationUrl(locationId);
        Retrofit retrofit = getRetrofitInstance();

        String token = Hawk.get(Constants.TOKEN);
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<UserRpcProto.UserBaseResponse> makeLocationDefaultObservable =
                service.makeLocationDefault(token, defaultLocationUrl);

        addSubscription(makeLocationDefaultObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse defLocationResponse) {
                        getView().hideProgressBar();
                        GlobalUtils.showLog(TAG, "make location def response: "
                                + defLocationResponse);

                        if (defLocationResponse == null) {
                            getView().onMakeLocationDefaultFail("Failed to set def location");
                            return;
                        }

                        if (defLocationResponse.getError()) {
                            getView().onMakeLocationDefaultFail(defLocationResponse.getMsg());
                            return;
                        }

                        getView().onMakeLocationDefualtSuccess(locationId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onMakeLocationDefaultFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    @Override
    public void deleteLocation(String locationId, int pos) {
        getView().showProgressBar("Please wait...");
        String deleteLocationUrl = getDeleteLocationUrl(locationId);
        GlobalUtils.showLog(TAG, "location Id check: " + deleteLocationUrl);
        Retrofit retrofit = getRetrofitInstance();

        String token = Hawk.get(Constants.TOKEN);
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<UserRpcProto.UserBaseResponse> deleteLocationObservable =
                service.deleteLocation(token, deleteLocationUrl);

        addSubscription(deleteLocationObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse delLocationResponse) {
                        getView().hideProgressBar();
                        GlobalUtils.showLog(TAG, "delete location response: "
                                + delLocationResponse);

                        if (delLocationResponse == null) {
                            getView().onDeleteLocationFail("Failed to delete location");
                            return;
                        }

                        if (delLocationResponse.getError()) {
                            getView().onDeleteLocationFail(delLocationResponse.getMsg());
                            return;
                        }

                        getView().onDeleteLocationSuccess(locationId, pos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onDeleteLocationFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    private Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

        String base_url = Hawk.get(Constants.BASE_URL);
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(base_url)
                .addConverterFactory(ProtoConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private String getDeleteLocationUrl(String locationId) {
        return "settings/rmlocation/" + locationId;
    }

    private String getDefaultLocationUrl(String locationId) {
        return "settings/deflocation/" + locationId;
    }
}
