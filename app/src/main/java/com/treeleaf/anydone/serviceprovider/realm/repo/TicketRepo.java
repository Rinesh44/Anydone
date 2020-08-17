package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.google.android.gms.common.util.CollectionUtils;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class TicketRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final TicketRepo ticketRepo;
    private static final String TAG = "TicketRepo";

    static {
        ticketRepo = new TicketRepo();
    }

    public static TicketRepo getInstance() {
        return ticketRepo;
    }

    public void saveTicketList(final List<TicketProto.Ticket> ticketListPb,
                               String type,
                               final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        try {
            realm.executeTransaction(realm1 -> {
                List<Tickets> ticketsList =
                        transformTicketProto(ticketListPb, type);
                realm1.copyToRealmOrUpdate(ticketsList);
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

    public void closeServiceRequest(long id) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<ServiceRequest> result = realm1.where(ServiceRequest.class)
                    .equalTo("serviceOrderId", id).findAll();
            String status = OrderServiceProto.ServiceOrderState.CLOSED_SERVICE_ORDER.name();
            result.setString("status", status);
        });
    }

    public List<Tickets> transformTicketProto
            (List<TicketProto.Ticket> ticketListPb, String type) {
        if (CollectionUtils.isEmpty(ticketListPb)) {
            throw new IllegalArgumentException(EXCEPTION_NULL_VALUE);
        }

        List<Tickets> ticketsList = new ArrayList<>();
        for (TicketProto.Ticket ticketPb : ticketListPb
        ) {
            Tickets tickets = new Tickets();
            tickets.setTicketId(ticketPb.getTicketId());
            tickets.setTitle(ticketPb.getTitle());
            tickets.setDescription(ticketPb.getDescription());
            tickets.setCustomer(ProtoMapper.transformCustomer(ticketPb.getCustomer()));
            tickets.setServiceProvider(ProtoMapper.transformServiceProvider(ticketPb.getServiceProvider()));
            tickets.setTicketSource(ticketPb.getTicketSource().name());
            tickets.setTagsRealmList(ProtoMapper.transformTags(ticketPb.getTagsList()));
            tickets.setServiceId(ticketPb.getService().getServiceId());
            tickets.setAssignedEmployee(ProtoMapper.transformAssignedEmployee(ticketPb.getEmployeesAssignedList()));
            tickets.setCustomerType(ticketPb.getCustomerType().name());
            tickets.setCreatedAt(ticketPb.getCreatedAt());
            tickets.setTicketType(type);
            tickets.setTicketStatus(ticketPb.getTicketState().name());
            ticketsList.add(tickets);
        }

        return ticketsList;
    }


    public List<Tickets> getAllTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Tickets.class).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Tickets> getAssignedTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Tickets.class).equalTo("ticketType",
                    Constants.ASSIGNED).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }


    public List<Tickets> getSubscribedTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Tickets.class).equalTo("ticketType",
                    Constants.SUBSCRIBED).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Tickets> getClosedResolvedTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Tickets.class).equalTo("ticketType",
                    Constants.CLOSED_RESOLVED).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Tickets> getAssignableTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Tickets.class).equalTo("ticketType",
                    Constants.ASSIGNABLE).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Tickets> getSubscribeableTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(Tickets.class).equalTo("ticketType",
                    Constants.SUBSCRIBEABLE).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }
}