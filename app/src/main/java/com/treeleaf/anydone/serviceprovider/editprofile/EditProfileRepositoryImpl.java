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
    public Observable<UserRpcProto.UserBaseResponse> editEmployeeProfile(@NonNull UserProto.EmployeeProfile employeeProfile) {
        return service.editEmployeeProfile(Hawk.get(Constants.TOKEN), employeeProfile);
    }

    @Override
    public Observable<UserRpcProto.UserBaseResponse> editServiceProviderProfile(@NonNull UserProto.ServiceProviderProfile serviceProviderProfile) {
        return service.editServiceProviderProfile(Hawk.get(Constants.TOKEN), serviceProviderProfile);
    }
}
