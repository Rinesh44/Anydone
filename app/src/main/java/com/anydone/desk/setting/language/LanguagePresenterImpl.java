package com.anydone.desk.setting.language;

import com.orhanobut.hawk.Hawk;
import com.anydone.desk.base.presenter.BasePresenter;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LanguagePresenterImpl extends BasePresenter<LanguageContract.LanguageView> implements
        LanguageContract.LanguagePresenter {
    private static final String TAG = "LanguagePresenterImpl";

    private LanguageRepository languageRepository;

    @Inject
    public LanguagePresenterImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public void changeLanguage(String language) {
        Preconditions.checkNotNull(language, "Language cannot be null");

        getView().showProgressBar("Please wait...");
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        Observable<UserRpcProto.UserBaseResponse> languageObservable =
                service.changeLanguage(token, language);


        addSubscription(languageObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse languageResponse) {
                        GlobalUtils.showLog(TAG, "change language response: " +
                                languageResponse);
                        if (languageResponse == null) {
                            getView().hideProgressBar();
                            getView().onLanguageChangeFail("Change language failed");
                            return;
                        }

                        if (languageResponse.getError()) {
                            getView().hideProgressBar();
                            getView().onLanguageChangeFail(languageResponse.getMsg());
                            return;
                        }

                        getView().onLanguageChangedSuccess(language);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onLanguageChangeFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));

    }
}
