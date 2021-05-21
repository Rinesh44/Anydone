package com.anydone.desk.realm.repo;


import com.treeleaf.anydone.entities.TicketProto;
import com.anydone.desk.realm.model.TicketCategory;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class TicketCategoryRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final String TAG = "TicketCategoryRepo";
    private static final TicketCategoryRepo ticketCategoryRepo;

    static {
        ticketCategoryRepo = new TicketCategoryRepo();
    }

    public static TicketCategoryRepo getInstance() {
        return ticketCategoryRepo;
    }

    public void saveTicketTypeList(final List<TicketProto.TicketType> ticketTypeList,
                                   final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(transformTicketTypeList(ticketTypeList));
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private List<TicketCategory> transformTicketTypeList(List<TicketProto.TicketType> ticketTypeListPb) {
        RealmList<TicketCategory> ticketTypeList = new RealmList<>();
        for (TicketProto.TicketType ticketTypePb : ticketTypeListPb
        ) {
            TicketCategory category = transformTicketType(ticketTypePb);
            ticketTypeList.add(category);
        }
        return ticketTypeList;
    }

    private TicketCategory transformTicketType(TicketProto.TicketType ticketTypePb) {
        TicketCategory category = new TicketCategory();
        category.setCategoryId(ticketTypePb.getTicketTypeId());
        category.setCreatedAt(ticketTypePb.getCreatedAt());
        category.setName(ticketTypePb.getName());
        category.setServiceId(ticketTypePb.getServiceId());
        category.setSpAccountId(ticketTypePb.getSpAccountId());
        category.setUpdatedAt(ticketTypePb.getUpdatedAt());
        return category;
    }

    public List<TicketCategory> getAllTicketCategories() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return new ArrayList<>(realm.where(TicketCategory.class)
                    .findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

}
