package com.anydone.desk.realm.repo;

import com.anydone.desk.realm.model.ConversationThreadLabel;
import com.anydone.desk.utils.Constants;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ConversationProto;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ConversationThreadLabelRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ConversationThreadLabelRepo labelRepo;
    private static final String TAG = "ConversationThreadLabel";

    static {
        labelRepo = new ConversationThreadLabelRepo();
    }

    public static ConversationThreadLabelRepo getInstance() {
        return labelRepo;
    }

    public void saveLabelList(final List<ConversationProto.ConversationLabel> labelList, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(transformLabelList(labelList));
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private List<ConversationThreadLabel> transformLabelList(List<ConversationProto.ConversationLabel> labelListPb) {
        List<ConversationThreadLabel> labelList = new ArrayList<>();
        for (ConversationProto.ConversationLabel labelPb : labelListPb
        ) {
            ConversationThreadLabel label = transformLabel(labelPb);
            labelList.add(label);
        }
        return labelList;
    }

    private ConversationThreadLabel transformLabel(ConversationProto.ConversationLabel labelPb) {
        ConversationThreadLabel label = new ConversationThreadLabel();
        label.setCreatedAt(labelPb.getCreatedAt());
        label.setLabelId(labelPb.getId());
        label.setName(labelPb.getName());
        label.setServiceId(labelPb.getServiceId());
        label.setUpdatedAt(labelPb.getUpdatedAt());
        return label;
    }

    public List<ConversationThreadLabel> getAllLabels() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(ConversationThreadLabel.class)
                    .equalTo("serviceId", serviceId)
                    .findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public ConversationThreadLabel getLabelById(String labelId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(ConversationThreadLabel.class)
                    .equalTo("labelId", labelId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

}
