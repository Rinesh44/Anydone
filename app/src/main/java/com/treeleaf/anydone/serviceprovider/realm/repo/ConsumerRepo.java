package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Consumer;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import io.realm.Realm;

public class ConsumerRepo extends Repo {
    private static final ConsumerRepo consumerRepo;

    static {
        consumerRepo = new ConsumerRepo();
    }


    public static ConsumerRepo getInstance() {
        return consumerRepo;
    }

    public void saveConsumer(final AuthProto.LoginResponse loginResponse, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        try {
            realm.executeTransaction(realm1 -> {
                Consumer consumer = setConsumer(loginResponse.getUser().getConsumer(), realm1);
                realm1.copyToRealmOrUpdate(consumer);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private Consumer setConsumer(UserProto.ConsumerProfile consumerProfilePb, Realm realm) {
        Consumer consumer = realm.where(Consumer.class)
                .equalTo(Consumer.CONSUMER_ID, consumerProfilePb.getConsumerProfileId())
                .findFirst();
        if (consumer != null) return consumer;
        return transformConsumer(realm.createObject(Consumer.class,
                consumerProfilePb.getConsumerProfileId()), consumerProfilePb);
    }

    private Consumer transformConsumer(Consumer consumer,
                                       UserProto.ConsumerProfile consumerProfile) {
        consumer.setAccountId(consumerProfile.getAccount().getAccountId());
        return consumer;
    }

    public Consumer getConsumerByAccountId(String accountId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        return realm.where(Consumer.class)
                .equalTo("accountId", accountId)
                .findFirst();
    }
}
