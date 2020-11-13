package com.treeleaf.anydone.serviceprovider.realm.repo;


import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketSuggestion;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class TicketSuggestionRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final String TAG = "TicketSuggestionRepo";
    private static final TicketSuggestionRepo ticketSuggestionRepo;

    static {
        ticketSuggestionRepo = new TicketSuggestionRepo();
    }

    public static TicketSuggestionRepo getInstance() {
        return ticketSuggestionRepo;
    }

    public void saveTicketSuggestionList(final List<TicketProto.TicketSuggestion> ticketSuggestionList,
                                         long estimatedTime,
                                         final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(transformTicketSuggestions(ticketSuggestionList,
                        estimatedTime));
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public List<TicketSuggestion> getAllTicketSuggestions() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(TicketSuggestion.class)
                    .sort("createdAt", Sort.DESCENDING)
                    .equalTo("serviceId", serviceId)
                    .findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public void deleteTicketSuggestionById(List<TicketSuggestion> suggestionList) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            for (TicketSuggestion suggestion : suggestionList
            ) {
                RealmResults<TicketSuggestion> result = realm1.where(TicketSuggestion.class)
                        .equalTo("suggestionId", suggestion.getSuggestionId()).findAll();
                result.deleteAllFromRealm();
            }
        });
    }

    public void deleteTicketSuggestionById(String id) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<TicketSuggestion> result = realm1.where(TicketSuggestion.class)
                    .equalTo("suggestionId", id).findAll();
            result.deleteAllFromRealm();
        });
    }

    public void deleteAllTicketSuggestions() {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<TicketSuggestion> result = realm1.where(TicketSuggestion.class).findAll();
            result.deleteAllFromRealm();
        });
    }

    private List<TicketSuggestion> transformTicketSuggestions(
            List<TicketProto.TicketSuggestion>
                    ticketSuggestionsListPb, long estimatedTime) {
        List<TicketSuggestion> ticketSuggestionList = new ArrayList<>();
        for (TicketProto.TicketSuggestion suggestionPb : ticketSuggestionsListPb
        ) {
            TicketSuggestion suggestion = new TicketSuggestion();
            suggestion.setConversationId(suggestionPb.getConversationId());
            suggestion.setCreatedAt(suggestionPb.getCreatedAt());
            suggestion.setCustomerId(suggestionPb.getCustomer().getCustomerId());
            suggestion.setCustomerImageUrl(suggestionPb.getCustomer().getProfilePic());
            suggestion.setCustomerName(suggestionPb.getCustomer().getFullName());
            suggestion.setEstimatedTime(estimatedTime);
            suggestion.setMessageId(suggestionPb.getMsg().getMsgId());
            suggestion.setMessageSentAt(suggestionPb.getMsg().getTimestamp());
            suggestion.setMessageText(suggestionPb.getMsg().getText());
            suggestion.setServiceId(suggestionPb.getServiceId());
            suggestion.setSource(suggestionPb.getSource().name());
            suggestion.setStatus(suggestionPb.getStatus().name());
            suggestion.setSuggestionId(suggestionPb.getSuggestionId());
            suggestion.setUpdatedAt(suggestionPb.getUpdatedAt());
            ticketSuggestionList.add(suggestion);
        }

        return ticketSuggestionList;
    }

}
