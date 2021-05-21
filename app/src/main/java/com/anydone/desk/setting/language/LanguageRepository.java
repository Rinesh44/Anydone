package com.anydone.desk.setting.language;

import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface LanguageRepository {
    Observable<UserRpcProto.UserBaseResponse> changeLanguage(String token, String language);
}
