package com.treeleaf.anydone.serviceprovider.realm.repo;


import com.google.android.gms.common.util.CollectionUtils;
import com.treeleaf.anydone.serviceprovider.realm.model.Location;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class LocationRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final LocationRepo locationRepo;

    static {
        locationRepo = new LocationRepo();
    }

    public static LocationRepo getInstance() {
        return locationRepo;
    }

    public void saveLocation(final Location location, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                List<Location> allLocationList = getAllLocation();
                if (CollectionUtils.isEmpty(allLocationList)) {
                    location.setDefault(true);
                }
                realm1.copyToRealmOrUpdate(location);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void saveLocationList(final List<Location> locationList, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(locationList);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }


    public List<Location> getAllLocation() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Location.class).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public void deleteLocationById(String id) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<Location> result = realm1.where(Location.class)
                    .equalTo("id", id).findAll();
            result.deleteAllFromRealm();
        });
    }

    public void setLocationAsPrimary(String id) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<Location> result = realm1.where(Location.class)
                    .equalTo("id", id).findAll();
            result.setBoolean("isDefault", true);
        });
    }

    public void removeLocationAsPrimary() {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<Location> result = realm1.where(Location.class)
                    .equalTo("isDefault", true).findAll();
            result.setBoolean("isDefault", false);
        });
    }

    public Location getDefaultLocation() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        return realm.where(Location.class)
                .equalTo("isDefault", true)
                .findFirst();
    }
}

