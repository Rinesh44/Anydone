package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.google.android.gms.common.util.CollectionUtils;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;

public class TagRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final TagRepo tagRepo;
    private static final String TAG = "TagRepo";

    static {
        tagRepo = new TagRepo();
    }

    public static TagRepo getInstance() {
        return tagRepo;
    }

    public void saveTags(final List<TicketProto.TicketTag> tagListPb,
                         final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                List<Tags> tagList =
                        transformTicketTagProto(tagListPb);
                realm1.copyToRealmOrUpdate(tagList);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public Tickets getTicketById(long ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return realm.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Tags> transformTicketTagProto
            (List<TicketProto.TicketTag> tagListPb) {
        if (CollectionUtils.isEmpty(tagListPb)) {
            throw new IllegalArgumentException(EXCEPTION_NULL_VALUE);
        }

        List<Tags> tagList = new ArrayList<>();
        for (TicketProto.TicketTag tagPb : tagListPb
        ) {
            Tags tag = new Tags();
            tag.setTagId(tagPb.getTagId());
            tag.setLabel(tagPb.getLabel());
            tag.setDescription(tagPb.getDescription());
            tag.setCreatedBy(tagPb.getCreatedBy().getAccount().getAccountId());
            tagList.add(tag);
        }

        return tagList;
    }

    public List<Tags> getAllTags() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Tags.class).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Tags> searchTags(String query) {
        final Realm realm = RealmUtils.getInstance().getRealm();
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