package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ConversationProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class ThreadRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ThreadRepo threadRepo;
    private static final String TAG = "ThreadRepo";

    static {
        threadRepo = new ThreadRepo();
    }

    public static ThreadRepo getInstance() {
        return threadRepo;
    }

    public void saveThreads(final List<ConversationProto.ConversationThread> threadListPb,
                            final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmList<Thread> threadList =
                        transformThread(threadListPb);

                removeDeletedItems(threadList);
                if (!CollectionUtils.isEmpty(threadList))
                    realm1.copyToRealmOrUpdate(threadList);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private void removeDeletedItems(RealmList<Thread> threadList) {
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        RealmResults<Thread> oldThreadList = getThreadsByServiceId(selectedService);
        oldThreadList.deleteAllFromRealm();
  /*      RealmList<Thread> diffList = new RealmList<>();
        for (Thread thread : oldThreadList
        ) {
            if (!threadList.contains(thread)) {
                diffList.add(thread);
            }
        }
        if (!CollectionUtils.isEmpty(diffList))
            diffList.deleteAllFromRealm();*/
    }


    public void enableBotReply(String threadId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Thread> result = realm1.where(Thread.class)
                    .equalTo("threadId", threadId).findAll();
            result.setBoolean("botEnabled", true);
        });
    }

    public void disableBotReply(String threadId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Thread> result = realm1.where(Thread.class)
                    .equalTo("threadId", threadId).findAll();
            result.setBoolean("botEnabled", false);
        });
    }

/*    private List<Thread> transformThread(List<ConversationProto.ConversationThread> threadListPb) {
        List<Thread> threadList = new ArrayList<>();
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        List<Thread> existingThreads = getThreadsByServiceId(serviceId);
        for (ConversationProto.ConversationThread threadPb : threadListPb
        ) {
            if (!CollectionUtils.isEmpty(existingThreads)) {
                GlobalUtils.showLog(TAG, "collection not empty");
                for (Thread thread : existingThreads
                ) {
                    if (thread.getThreadId().equalsIgnoreCase(threadPb.getConversationId())) {
                        GlobalUtils.showLog(TAG, "current timst: " + threadPb.getMessage().getTimestamp());
                        GlobalUtils.showLog(TAG, "existing timst: " + thread.getLastMessageDate());
                        if (thread.getLastMessageDate() != threadPb.getMessage().getTimestamp()) {
                            GlobalUtils.showLog(TAG, "not equals");
                            updateThread(thread, System.currentTimeMillis(), threadPb.getMessage()
                                            .getTimestamp(), threadPb.getMessage().getMessage().getText(),
                                    false);
                        }
                    }
                }
            } else {
                GlobalUtils.showLog(TAG, "last one entered");
                Thread newThread = createNewThread(threadPb);
                threadList.add(newThread);
            }
        }
        return threadList;
    }*/

    private RealmList<Thread> transformThread(List<ConversationProto.ConversationThread> threadListPb) {
        RealmList<Thread> threadList = new RealmList<>();
        for (ConversationProto.ConversationThread threadPb : threadListPb
        ) {
            Thread thread = new Thread();
            if (!CollectionUtils.isEmpty(threadPb.getEmployeeAssignedList())) {
                thread.setAssignedEmployee(ProtoMapper.transformAssignedEmployee(
                        threadPb.getEmployeeAssigned(0)));
            }
            thread.setBotEnabled(true);
            thread.setCreatedAt(threadPb.getCreatedAt());
            thread.setCustomerEmail(threadPb.getCustomer().getEmail());
            thread.setCustomerId(threadPb.getCustomer().getCustomerId());
            thread.setCustomerImageUrl(threadPb.getCustomer().getProfilePic());
            thread.setCustomerName(threadPb.getCustomer().getFullName());
            thread.setCustomerPhone(threadPb.getCustomer().getPhone());
            thread.setDefaultLabelId(threadPb.getTag().getTagId());
            thread.setDefaultLabel(threadPb.getTag().getLabel());
            thread.setFinalMessage(threadPb.getMessage().getMessage().getText());
            thread.setLastMessageDate(threadPb.getMessage().getTimestamp());
            thread.setServiceId(threadPb.getServiceId());
            thread.setServiceProviderId(threadPb.getServiceProviderId());
            thread.setSource(threadPb.getSource().name());
            thread.setThreadId(threadPb.getConversationId());
            thread.setUpdatedAt(threadPb.getUpdatedAt());
            thread.setBotEnabled(threadPb.getBotEnabled());
            thread.setSeen(true);
            threadList.add(thread);
        }
        return threadList;
    }

    public void setSeenStatus(Thread thread) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            GlobalUtils.showLog(TAG, "updateSeenStatus()");
            realm.executeTransaction(realm1 -> {
                thread.setSeen(true);
                realm.copyToRealmOrUpdate(thread);
            });
        } catch (Throwable throwable) {
            GlobalUtils.showLog(TAG, "error thread update: " + throwable.getLocalizedMessage());
            throwable.printStackTrace();
        } finally {
            close(realm);
        }
    }

    private Thread createNewThread(ConversationProto.ConversationThread threadPb) {
        Thread newThread = new Thread();
        if (!CollectionUtils.isEmpty(threadPb.getEmployeeProfileList())) {
            newThread.setAssignedEmployee(ProtoMapper.transformEmployee
                    (threadPb.getEmployeeProfileList()).get(0));
        }
        newThread.setBotEnabled(true);
        newThread.setCreatedAt(threadPb.getCreatedAt());
        newThread.setCustomerEmail(threadPb.getCustomer().getEmail());
        newThread.setCustomerId(threadPb.getCustomer().getCustomerId());
        newThread.setCustomerImageUrl(threadPb.getCustomer().getProfilePic());
        newThread.setCustomerName(threadPb.getCustomer().getFullName());
        newThread.setCustomerPhone(threadPb.getCustomer().getPhone());
        newThread.setDefaultLabelId(threadPb.getTag().getTagId());
        newThread.setDefaultLabel(threadPb.getTag().getLabel());
        newThread.setFinalMessage(threadPb.getMessage().getMessage().getText());
        newThread.setLastMessageDate(threadPb.getMessage().getTimestamp());
        newThread.setServiceId(threadPb.getServiceId());
        newThread.setServiceProviderId(threadPb.getServiceProviderId());
        newThread.setSource(threadPb.getSource().name());
        newThread.setThreadId(threadPb.getConversationId());
        newThread.setUpdatedAt(threadPb.getUpdatedAt());
        newThread.setBotEnabled(threadPb.getBotEnabled());
        newThread.setSeen(false);
        return newThread;
    }

    public void updateThread(final Thread thread,
                             long updatedAt,
                             long lastMessageDate,
                             String lastMessage,
                             boolean seen,
                             final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            GlobalUtils.showLog(TAG, "updateThread()");
            realm.executeTransaction(realm1 -> {
                thread.setUpdatedAt(updatedAt);
                thread.setLastMessageDate(lastMessageDate);
                thread.setFinalMessage(lastMessage);
                thread.setSeen(seen);
                realm.copyToRealmOrUpdate(thread);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            GlobalUtils.showLog(TAG, "error thread update: " + throwable.getLocalizedMessage());
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public Thread getThreadById(String threadId) {
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

    public RealmResults<Thread> getThreadsByServiceId(String serviceId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return realm.where(Thread.class)
                    .equalTo("serviceId", serviceId)
                    .sort("lastMessageDate", Sort.DESCENDING)
                    .findAll();
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