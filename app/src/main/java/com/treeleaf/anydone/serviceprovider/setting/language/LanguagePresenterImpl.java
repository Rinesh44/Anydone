package com.treeleaf.anydone.serviceprovider.setting.language;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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
        Observable<UserRpcProto.UserBaseResponse> languageObservable =
                languageRepository.changeLanguage(token, language);

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
