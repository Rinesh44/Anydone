package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ThreadRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ThreadRepo threadRepo;

    static {
        threadRepo = new ThreadRepo();
    }

    public static ThreadRepo getInstance() {
        return threadRepo;
    }

/*    public void saveThreads(final List<TicketProto.TicketTag> threadListPb,
                            final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                List<Thread> threadList =
                        transformThread(threadListPb);
                realm1.copyToRealmOrUpdate(threadList);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }*/

    public Thread getThreadById(long threadId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return realm.where(Thread.class)
                    .equalTo("threadId", threadId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }


    public List<Thread> getAllThreads() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Thread.class).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

}