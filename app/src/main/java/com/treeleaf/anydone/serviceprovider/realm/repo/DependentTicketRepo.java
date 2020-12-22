package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.DependentTicket;
import com.treeleaf.anydone.serviceprovider.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

public class DependentTicketRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final String TAG = "DependentTicketRepo";
    private static final DependentTicketRepo dependentTicketRepo;

    static {
        dependentTicketRepo = new DependentTicketRepo();
    }

    public static DependentTicketRepo getInstance() {
        return dependentTicketRepo;
    }

    public void saveTicketList(final List<TicketProto.Ticket> ticketList, final Repo.Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(transformTicketList(ticketList));
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private List<DependentTicket> transformTicketList(List<TicketProto.Ticket> ticketListPb) {
        RealmList<DependentTicket> ticketRealmList = new RealmList<>();
        for (TicketProto.Ticket ticketPb : ticketListPb
        ) {
            DependentTicket ticket = transformTickets(ticketPb);
            ticketRealmList.add(ticket);
        }
        return ticketRealmList;
    }

    private DependentTicket transformTickets(TicketProto.Ticket ticketPb) {
        DependentTicket ticket = new DependentTicket();
        ticket.setId(ticketPb.getTicketId());
        ticket.setIndex(ticketPb.getTicketIndex());
        ticket.setSummary(ticketPb.getTitle());
        ticket.setCreatedAt(ticketPb.getCreatedAt());
        ticket.setServiceId(ticketPb.getService().getServiceId());
        return ticket;
    }

    public List<DependentTicket> getAllDependentTickets() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(DependentTicket.class)
                    .equalTo("serviceId", serviceId)
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
