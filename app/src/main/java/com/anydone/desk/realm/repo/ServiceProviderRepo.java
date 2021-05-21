package com.anydone.desk.realm.repo;


import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.entities.UserProto;
import com.anydone.desk.realm.model.ServiceProvider;

import io.realm.Realm;

public class ServiceProviderRepo extends Repo {
    private static final ServiceProviderRepo SERVICE_PROVIDER_REPO;

    static {
        SERVICE_PROVIDER_REPO = new ServiceProviderRepo();
    }


    public static ServiceProviderRepo getInstance() {
        return SERVICE_PROVIDER_REPO;
    }

    public void saveServiceProvider(final AuthProto.LoginResponse loginResponse, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {
                ServiceProvider serviceProvider = setServiceProvider(loginResponse.getUser().getServiceProvider(), realm1);
                realm1.copyToRealmOrUpdate(serviceProvider);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private ServiceProvider setServiceProvider(UserProto.ServiceProviderProfile serviceProviderProfilePb, Realm realm) {
        ServiceProvider serviceProvider = realm.where(ServiceProvider.class)
                .equalTo(ServiceProvider.SERVICE_PROVIDER_ID, serviceProviderProfilePb.getServiceProviderProfileId())
                .findFirst();
        if (serviceProvider != null) return serviceProvider;
        return transformServiceProvider(realm.createObject(ServiceProvider.class,
                serviceProviderProfilePb.getServiceProviderProfileId()), serviceProviderProfilePb);
    }

    private ServiceProvider transformServiceProvider(ServiceProvider serviceProvider,
                                                     UserProto.ServiceProviderProfile serviceProviderProfile) {
        serviceProvider.setAccountId(serviceProviderProfile.getAccount().getAccountId());

        serviceProvider.setFullName(serviceProviderProfile.getAccount().getFullName());
        serviceProvider.setProfilePic(serviceProviderProfile.getAccount().getProfilePic());
        serviceProvider.setCreatedAt(serviceProviderProfile.getCreatedAt());
        serviceProvider.setPhone(serviceProviderProfile.getAccount().getPhone());
        serviceProvider.setEmail(serviceProviderProfile.getAccount().getEmail());
        return serviceProvider;
    }

    public ServiceProvider getServiceProviderByAccountId(String accountId) {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(ServiceProvider.class)
                .equalTo("accountId", accountId)
                .findFirst();
    }

    public ServiceProvider getServiceProvider() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(ServiceProvider.class).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }
}

