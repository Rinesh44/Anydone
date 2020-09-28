package com.treeleaf.anydone.serviceprovider.realm.repo;


import com.google.android.gms.common.util.CollectionUtils;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AvailableServicesRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final AvailableServicesRepo availableServicesRepo;

    static {
        availableServicesRepo = new AvailableServicesRepo();
    }

    public static AvailableServicesRepo getInstance() {
        return availableServicesRepo;
    }

    public void saveAvailableServices(final List<ServiceProto.Service> serviceListPb,
                                      final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        try {
            realm.executeTransaction(realm1 -> {
                List<Service> availableServiceList = transformServicesProto(serviceListPb);
                realm1.copyToRealmOrUpdate(availableServiceList);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }


    private static List<Service> transformServicesProto(List<ServiceProto.Service> serviceListPb) {
        if (CollectionUtils.isEmpty(serviceListPb)) {
            throw new IllegalArgumentException(EXCEPTION_NULL_VALUE);
        }

        List<Service> serviceList = new ArrayList<>();
        for (ServiceProto.Service servicePb : serviceListPb
        ) {
            if (servicePb.getServiceType().equals(ServiceProto.ServiceType.CORPORATE_SERVICE)) {
                Service service = new Service();
                service.setCreatedAt(servicePb.getCreatedAt());
                service.setDesc(servicePb.getDesc());
                service.setName(GlobalUtils.convertCase(servicePb.getName()));
                service.setServiceIconUrl(servicePb.getServiceIconUrl());
                service.setServiceId(servicePb.getServiceId());
                service.setServiceType(servicePb.getServiceType().name());

                serviceList.add(service);
            }
        }

        return serviceList;

    }

    public List<Service> getAvailableServices() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Service.class).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public Service getAvailableServiceById(String serviceId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return realm.where(Service.class)
                    .equalTo("serviceId", serviceId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    private RealmQuery<Service> performSearch(String searchTerm, Realm realm) {
        RealmQuery<Service> query = realm.where(Service.class);
        query = query
                .contains("name", searchTerm, Case.INSENSITIVE);
        return query;
    }

    public List<Service> searchService(String searchTerm) {
        String[] separated = searchTerm.split(" ");
        List<String> wordList = new ArrayList<>(Arrays.asList(separated));
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            RealmQuery<Service> result = performSearch(searchTerm, realm);
            RealmResults<Service> services = result.findAll();
            return new ArrayList<>(services);
        } catch (Exception throwable) {
            throwable.printStackTrace();
            return Collections.emptyList();
        }
    }
}