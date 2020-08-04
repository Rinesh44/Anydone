package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.serviceprovider.realm.model.Card;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class CardRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final CardRepo cardRepo;

    static {
        cardRepo = new CardRepo();
    }

    public static CardRepo getInstance() {
        return cardRepo;
    }

    public void saveCard(final Card card, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(card);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }


    public List<Card> getAllCards() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Card.class).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

}
