package com.treeleaf.anydone.serviceprovider.editprofile;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.Constants;

import io.reactivex.Observable;

public class EditProfileRepositoryImpl implements EditProfileRepository {
    AnyDoneService service;

    public EditProfileRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> editProfile
            (@NonNull UserProto.ConsumerProfile consumerProfile) {
        return service.editProfile(Hawk.get(Constants.TOKEN), consumerProfile);
    }
}
