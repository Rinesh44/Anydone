package com.anydone.desk.realm.repo;

import com.anydone.desk.realm.model.FilterData;
import com.anydone.desk.realm.model.Label;
import com.anydone.desk.realm.model.Location;
import com.anydone.desk.realm.model.Tickets;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class FilterDataRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final FilterDataRepo filterDataRepo;

    static {
        filterDataRepo = new FilterDataRepo();
    }

    public static FilterDataRepo getInstance() {
        return filterDataRepo;
    }

    public void saveFilterData(final FilterData filterData, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(filterData);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }


    public FilterData getFilterDataByServiceId(String serviceId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(FilterData.class)
                    .equalTo("serviceId", serviceId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public void deleteFilterData(FilterData data) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<FilterData> result = realm1.where(FilterData.class)
                    .equalTo("serviceId", data.getServiceId()).findAll();
            result.deleteAllFromRealm();
        });
    }

}