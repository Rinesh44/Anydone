package com.treeleaf.anydone.serviceprovider.setting.language;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public class LanguageRepositoryImpl implements LanguageRepository {
    private AnyDoneService service;
    private static final String TAG = "LanguageRepositoryImpl";

    public LanguageRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> changeLanguage(String token, String language) {
        return service.changeLanguage(token, language);
    }
}
