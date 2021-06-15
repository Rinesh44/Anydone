package com.anydone.desk.realm.repo;

import com.anydone.desk.realm.model.MessageFilterData;

import io.realm.Realm;
import io.realm.RealmResults;

public class MessageFilterDataRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final MessageFilterDataRepo filterDataRepo;

    static {
        filterDataRepo = new MessageFilterDataRepo();
    }

    public static MessageFilterDataRepo getInstance() {
        return filterDataRepo;
    }

    public void saveFilterData(final MessageFilterData filterData, final Callback callback) {
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


    public MessageFilterData getFilterDataByServiceId(String serviceId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(MessageFilterData.class)
                    .equalTo("serviceId", serviceId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public void deleteFilterData(MessageFilterData data) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<MessageFilterData> result = realm1.where(MessageFilterData.class)
                    .equalTo("serviceId", data.getServiceId()).findAll();
            result.deleteAllFromRealm();
        });
    }

}