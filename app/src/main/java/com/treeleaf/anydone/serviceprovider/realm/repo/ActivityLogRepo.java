package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.google.android.gms.common.util.CollectionUtils;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.ActivityLog;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;

public class ActivityLogRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ActivityLogRepo logRepo;
    private static final String TAG = "ActivityLogRepo";

    static {
        logRepo = new ActivityLogRepo();
    }

    public static ActivityLogRepo getInstance() {
        return logRepo;
    }

    public void saveLogs(final List<TicketProto.TicketActivityLog> logPb,
                         final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                List<ActivityLog> logList =
                        transformLogProto(logPb);
                realm1.copyToRealmOrUpdate(logList);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public ActivityLog getLogById(String logId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(ActivityLog.class)
                    .equalTo("logId", logId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<ActivityLog> transformLogProto
            (List<TicketProto.TicketActivityLog> logListPb) {
        if (CollectionUtils.isEmpty(logListPb)) {
            throw new IllegalArgumentException(EXCEPTION_NULL_VALUE);
        }

        List<ActivityLog> logList = new ArrayList<>();
        for (TicketProto.TicketActivityLog logPb : logListPb
        ) {
            ActivityLog log = new ActivityLog();
            log.setAccountId(logPb.getAccount().getAccountId());
            log.setFullName(logPb.getAccount().getFullName());
            log.setLogId(logPb.getLogId());
            log.setProfilePic(logPb.getAccount().getProfilePic());
            log.setActivityType(logPb.getActivityType().name());
            log.setCreatedAt(logPb.getCreatedAt());
            log.setNewValue(logPb.getNewValue());
            log.setOldValue(logPb.getOldValue());
            log.setTicketId(logPb.getTicketId());
            log.setValue(logPb.getValue());
            logList.add(log);
        }

        return logList;
    }

    public List<ActivityLog> getAllLogs(long ticketId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return new ArrayList<>(realm.where(ActivityLog.class)
                    .equalTo("ticketId", ticketId)
                    .findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Tags> searchTags(String query) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            RealmQuery<Tags> result = performSearch(query, realm);
            return result.findAll();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    private RealmQuery<Tags> performSearch(String searchTerm, Realm realm) {
        RealmQuery<Tags> query = realm.where(Tags.class);
        query = query
                .contains("label", searchTerm, Case.INSENSITIVE);
        return query;
    }


}