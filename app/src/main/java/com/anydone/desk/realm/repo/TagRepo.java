package com.anydone.desk.realm.repo;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

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

    public void saveTags(final List<TicketProto.Team> tagListPb,
                         final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
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

    public Tags getTagById(String tagId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Tags.class)
                    .equalTo("tagId", tagId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Tags> transformTicketTagProto
            (List<TicketProto.Team> tagListPb) {
        if (CollectionUtils.isEmpty(tagListPb)) {
            throw new IllegalArgumentException(EXCEPTION_NULL_VALUE);
        }

        List<Tags> tagList = new ArrayList<>();
        for (TicketProto.Team tagPb : tagListPb
        ) {
            Tags tag = new Tags();
            tag.setTagId(tagPb.getTeamId());
            tag.setLabel(tagPb.getLabel());
            tag.setServiceId(tagPb.getServiceId());
            tag.setDescription(tagPb.getDescription());
            tag.setCreatedBy(tagPb.getCreatedBy().getAccount().getAccountId());
            tagList.add(tag);
        }

        return tagList;
    }

    public List<Tags> getAllTags() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            GlobalUtils.showLog(TAG, "get all tags()");
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            GlobalUtils.showLog(TAG, "service id: " + serviceId);
            return new ArrayList<>(realm.where(Tags.class)
                    .equalTo("serviceId", serviceId)
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
