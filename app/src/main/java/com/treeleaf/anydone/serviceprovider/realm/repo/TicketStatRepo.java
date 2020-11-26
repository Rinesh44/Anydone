package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByDate;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByPriority;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByResolvedTime;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatBySource;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByStatus;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.List;

import io.realm.Realm;

public class TicketStatRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final String TAG = "TicketStatRepo";
    private static final TicketStatRepo statRepo;

    static {
        statRepo = new TicketStatRepo();
    }

    public static TicketStatRepo getInstance() {
        return statRepo;
    }

    public void saveTicketStatListByDate(List<TicketProto.TicketStatByStatus> ticketStatByStatusListPb,
                                         final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                TicketStatByDate ticketStatByDate = ProtoMapper
                        .transformTicketStatByDate(ticketStatByStatusListPb);
                realm1.copyToRealmOrUpdate(ticketStatByDate);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }


    public TicketStatByDate getTicketStatByDate() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(TicketStatByDate.class).findFirst();
        } catch (Throwable throwable) {
            GlobalUtils.showLog(TAG, "throwable result: " + throwable);
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public void saveTicketByStatus(TicketProto.TicketStatByStatus ticketStatByStatusPb,
                                   final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                TicketStatByStatus ticketStatByStatus = ProtoMapper
                        .transformTicketByStatus(ticketStatByStatusPb, false);
                realm1.copyToRealmOrUpdate(ticketStatByStatus);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public TicketStatByStatus getTicketStatByStatus() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(TicketStatByStatus.class).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public void saveTicketByPriority(TicketProto.TicketStatByPriority ticketStatByPriorityPb,
                                     final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                TicketStatByPriority ticketStatByPriority = ProtoMapper
                        .transformTicketByPriority(ticketStatByPriorityPb);
                realm1.copyToRealmOrUpdate(ticketStatByPriority);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public TicketStatByPriority getTicketStatByPriority() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(TicketStatByPriority.class).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }


    public void saveTicketBySource(TicketProto.TicketStatBySource ticketStatBySourcePb,
                                   final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                TicketStatBySource ticketStatBySource = ProtoMapper
                        .transformTicketBySource(ticketStatBySourcePb);
                realm1.copyToRealmOrUpdate(ticketStatBySource);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public TicketStatBySource getTicketStatBySource() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(TicketStatBySource.class).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }


    public void saveTicketByResolvedTime(TicketProto.TicketStatResolveTime ticketStatResolveTimePb,
                                         final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                TicketStatByResolvedTime ticketStatByResolvedTime = ProtoMapper
                        .transformTicketByResolvedTime(ticketStatResolveTimePb);
                realm1.copyToRealmOrUpdate(ticketStatByResolvedTime);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public TicketStatByResolvedTime getTicketStatByResolvedTime() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(TicketStatByResolvedTime.class).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }
}
