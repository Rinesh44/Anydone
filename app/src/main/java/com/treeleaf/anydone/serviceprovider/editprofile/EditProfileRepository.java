package com.treeleaf.anydone.serviceprovider.editprofile;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface EditProfileRepository {
    Observable<UserRpcProto.UserBaseResponse> editProfile(
            @NonNull UserProto.ConsumerProfile consumerProfile);

}
