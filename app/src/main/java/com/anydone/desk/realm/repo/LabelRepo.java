package com.anydone.desk.realm.repo;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.anydone.desk.realm.model.Label;
import com.anydone.desk.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class LabelRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final String TAG = "LabelRepo";
    private static final LabelRepo labelRepo;

    static {
        labelRepo = new LabelRepo();
    }

    public static LabelRepo getInstance() {
        return labelRepo;
    }

    public void saveLabelList(final List<TicketProto.Label> labelList, final Callback callback) {
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

    private List<Label> transformLabelList(List<TicketProto.Label> labelListPb) {
        List<Label> labelList = new ArrayList<>();
        for (TicketProto.Label labelPb : labelListPb
        ) {
            Label label = transformLabel(labelPb);
            labelList.add(label);
        }
        return labelList;
    }

    private Label transformLabel(TicketProto.Label labelPb) {
        Label label = new Label();
        label.setCreatedAt(labelPb.getCreatedAt());
        label.setLabelId(labelPb.getLabelId());
        label.setName(labelPb.getName());
        label.setServiceId(labelPb.getServiceId());
        label.setSpAccountId(labelPb.getSpAccountId());
        label.setUpdatedAt(labelPb.getUpdatedAt());
        return label;
    }

    public List<Label> getAllLabels() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(Label.class)
                    .equalTo("serviceId", serviceId)
                    .findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public Label getLabelById(String labelId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Label.class)
                    .equalTo("labelId", labelId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

}
