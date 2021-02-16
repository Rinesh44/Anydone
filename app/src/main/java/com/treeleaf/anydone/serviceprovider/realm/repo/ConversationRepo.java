package com.treeleaf.anydone.serviceprovider.realm.repo;


import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Receiver;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class ConversationRepo extends Repo {
    private static final String TAG = "ConversationRepo";
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ConversationRepo conversationRepo;

    static {
        conversationRepo = new ConversationRepo();
    }

    public static ConversationRepo getInstance() {
        return conversationRepo;
    }

    public void saveConversation(final Conversation conversation, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransactionAsync(realm1 -> {
                realm1.copyToRealmOrUpdate(conversation);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        }
    }


    public void saveConversationList(final List<Conversation> conversationList,
                                     final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(conversationList);
                callback.success(true);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void updateConversation(final Conversation conversation, String messageId,
                                   long sentAt, long savedAt, RealmList<Receiver> receiverLIst,
                                   String message, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            GlobalUtils.showLog(TAG, "updateConversation()");
            realm.executeTransaction(realm1 -> {
                RealmList<Receiver> realmReceiverList = createReceivers(receiverLIst, realm);
                conversation.setSent(true);
                conversation.setConversationId(messageId);
                conversation.setSentAt(sentAt);
                conversation.setSavedAt(savedAt);
                conversation.setMessage(message);
                conversation.setReceiverList(realmReceiverList);
                realm.copyToRealmOrUpdate(conversation);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void updateSeenStatus(final Conversation conversation, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                conversation.setMessageStatus("Seen");
                realm.copyToRealmOrUpdate(conversation);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void onConversationSendFailed(final Conversation conversation, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                conversation.setSendFail(true);
                realm.copyToRealmOrUpdate(conversation);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private RealmList<Receiver> createReceivers(RealmList<Receiver> receiverLIst, Realm realm) {
        RealmList<Receiver> receiverList1 = new RealmList<>();
        for (Receiver receiver : receiverLIst
        ) {
            Receiver receiver1 = realm.createObject(Receiver.class, receiver.getReceiverId());
            receiverList1.add(receiver1);
        }
        return receiverList1;
    }

    public List<Conversation> getConversationList() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return new ArrayList<>(realm.where(Conversation.class).findAllAsync());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Collections.emptyList();
        } finally {
            close(realm);
        }
    }

    public List<Conversation> getConversationByOrderId(String refId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            List<Conversation> conversationList = new ArrayList<>(realm.where(Conversation.class)
                    .equalTo("refId", refId)
                    .equalTo("sent", true)
                    .equalTo("sendFail", false)
                    .equalTo("isReply", false)
                    .equalTo("parentId", "")
//                    .findAllAsync().sort("sentAt", Sort.ASCENDING));
                    .findAllAsync());

            Collections.sort(conversationList, (o1, o2) ->
                    Long.compare(o2.getSentAt(), o1.getSentAt()));

            return conversationList;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Collections.emptyList();
        } finally {
            close(realm);
        }
    }

    public List<Conversation> getThreadConversationByOrderId(String refId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            //                    .findAllAsync());

            return new ArrayList<>(realm.where(Conversation.class)
                    .equalTo("refId", refId)
                    .equalTo("sent", true)
                    .equalTo("sendFail", false)
                    .equalTo("isReply", false)
                    .findAllAsync().sort("sentAt", Sort.ASCENDING));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Collections.emptyList();
        } finally {
            close(realm);
        }
    }

    public void deleteConversationById(String clientId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Conversation> result = realm1.where(Conversation.class)
                        .equalTo("clientId", clientId)
                        .findAll();
                result.deleteAllFromRealm();
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            close(realm);
        }
    }

    public Conversation getConversationByClientId(String clientId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Conversation.class)
                    .equalTo("clientId", clientId)
                    .equalTo("isReply", false)
                    .findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public Conversation getConversationByMessageId(String messageId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Conversation.class)
                    .equalTo("conversationId", messageId)
                    .equalTo("isReply", false)
                    .findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Conversation> getReplies(String messageId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Conversation.class)
                    .equalTo("parentId", messageId)
                    .findAllAsync();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Collections.emptyList();
        } finally {
            close(realm);
        }
    }
}