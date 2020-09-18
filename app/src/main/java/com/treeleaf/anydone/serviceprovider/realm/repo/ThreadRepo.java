package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.google.android.gms.common.util.CollectionUtils;
import com.treeleaf.anydone.entities.ConversationProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ThreadRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ThreadRepo threadRepo;

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

    private List<Thread> transformThread(List<ConversationProto.ConversationThread> threadListPb) {
        List<Thread> threadList = new ArrayList<>();
        for (ConversationProto.ConversationThread threadPb : threadListPb
        ) {
            Thread thread = new Thread();
            if (!CollectionUtils.isEmpty(threadPb.getEmployeeProfileList())) {
                thread.setAssignedEmployee(ProtoMapper.transformEmployee(threadPb.getEmployeeProfileList()).get(0));
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
            threadList.add(thread);
        }
        return threadList;
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

    public List<Thread> getThreadsByServiceId(String serviceId) {
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