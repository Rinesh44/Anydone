package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.entities.PaymentProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Card;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class CardRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final String TAG = "CardRepo";
    private static final CardRepo cardRepo;

    static {
        cardRepo = new CardRepo();
    }

    public static CardRepo getInstance() {
        return cardRepo;
    }

    public void saveCard(final PaymentProto.Card card, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(transformCard(card));
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void saveCardList(final List<PaymentProto.Card> cardList, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(transformCardList(cardList));
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private List<Card> transformCardList(List<PaymentProto.Card> cardListPb) {
        RealmList<Card> cardList = new RealmList<>();
        for (PaymentProto.Card cardPb : cardListPb
        ) {
            Card card = transformCard(cardPb);
            cardList.add(card);
        }
        return cardList;
    }

    public void deleteCardById(String id) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<Card> result = realm1.where(Card.class)
                    .equalTo("cardId", id).findAll();
            result.deleteAllFromRealm();
        });
    }

    public void setCardAsPrimary(String id) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<Card> result = realm1.where(Card.class)
                    .equalTo("cardId", id).findAll();
            result.setBoolean("primary", true);
        });
    }

    public void removeCardAsPrimary() {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<Card> result = realm1.where(Card.class)
                    .equalTo("primary", true).findAll();
            result.setBoolean("primary", false);
        });
    }

    private Card transformCard(PaymentProto.Card cardPb) {
        Card card = new Card();
        card.setAccountId(cardPb.getAccountId());
        card.setCardId(cardPb.getCardId());
        card.setRefId(cardPb.getRefId());
        card.setCardNumber(cardPb.getCardNumber());
        card.setExpiryDate(cardPb.getExpiryDate());
        card.setCvv(cardPb.getCvc());
        card.setStreetAddress(cardPb.getBillingAddress().getStreet());
        card.setCity(cardPb.getBillingAddress().getCity());
        card.setState(cardPb.getBillingAddress().getState());
        card.setCountryCode(cardPb.getBillingAddress().getCountryCode());
        card.setCardStatus(cardPb.getCardStatus().name());
        card.setCreatedAt(cardPb.getCreatedAt());
        card.setUpdatedAt(cardPb.getUpdatedAt());
        card.setCardType(cardPb.getCardType());
        card.setPrimary(cardPb.getIsDefault());
        return card;
    }

    public List<Card> getAllCards() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Card.class)
                    .sort("createdAt", Sort.DESCENDING)
                    .findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

}
