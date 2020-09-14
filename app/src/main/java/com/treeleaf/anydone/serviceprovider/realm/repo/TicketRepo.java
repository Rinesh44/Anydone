package com.treeleaf.anydone.serviceprovider.realm.repo;


import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

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

    public void saveTicket(final TicketProto.Ticket ticketPb,
                           String type,
                           final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        try {
            realm.executeTransaction(realm1 -> {
                Tickets ticket =
                        transformTicket(ticketPb, type);
                realm1.copyToRealmOrUpdate(ticket);
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

    public void setAssignedEmployee(long ticketId, Employee employee, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> result = realm1.where(Tickets.class)
                        .equalTo("ticketId", ticketId).findAll();
                result.setObject("assignedEmployee", employee);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void changeTicketStatusToStart(long ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            String status = TicketProto.TicketState.TICKET_STARTED.name();
            result.setString("ticketStatus", status);
        });
    }


    public void changeTicketStatusToClosed(long ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            String status = TicketProto.TicketState.TICKET_CLOSED.name();
            result.setString("ticketStatus", status);
            result.setString("ticketType", Constants.CLOSED_RESOLVED);
        });
    }

    public void changeTicketStatusToReopened(long ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            String status = TicketProto.TicketState.TICKET_REOPENED.name();
            result.setString("ticketStatus", status);

        });
    }

    public void changeTicketStatusToResolved(long ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            String status = TicketProto.TicketState.TICKET_RESOLVED.name();
            result.setString("ticketStatus", status);
            result.setString("ticketType", Constants.CLOSED_RESOLVED);
        });
    }

/*    public void unAssignEmployee(long ticketId, String empId, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            try {
                Tickets tickets = getTicketById(ticketId);
                Employee employeeToDel = null;
                for (Employee employee : tickets.getAssignedEmployee()
                ) {
                    if (employee.getEmployeeId().equalsIgnoreCase(empId)) {
                        employeeToDel = employee;
                    }
                }
                tickets.getAssignedEmployee().remove(employeeToDel);
                callback.success(null);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                callback.fail();
            }
        });
    }*/


    public void replaceAssignedEmployees(long ticketId, Employee employee, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            try {
                Tickets tickets = getTicketById(ticketId);
                tickets.setAssignedEmployee(employee);
                callback.success(null);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                callback.fail();
            }
        });
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
            tickets.setAssignedEmployee(ProtoMapper.transformAssignedEmployee(ticketPb.getEmployeeAssigned()));
            tickets.setCustomerType(ticketPb.getCustomerType().name());
            tickets.setCreatedAt(ticketPb.getCreatedAt());
            tickets.setTicketType(type);
            tickets.setTicketStatus(ticketPb.getTicketState().name());
            tickets.setCreatedByName(ticketPb.getCreatedBy().getAccount().getFullName());
            tickets.setCreatedByPic(ticketPb.getCreatedBy().getAccount().getProfilePic());
            ticketsList.add(tickets);
        }

        return ticketsList;
    }

    public Tickets transformTicket
            (TicketProto.Ticket ticketPb, String type) {

        Tickets tickets = new Tickets();
        tickets.setTicketId(ticketPb.getTicketId());
        tickets.setTitle(ticketPb.getTitle());
        tickets.setDescription(ticketPb.getDescription());
        tickets.setCustomer(ProtoMapper.transformCustomer(ticketPb.getCustomer()));
        tickets.setServiceProvider(ProtoMapper.transformServiceProvider(ticketPb.getServiceProvider()));
        tickets.setTicketSource(ticketPb.getTicketSource().name());
        tickets.setTagsRealmList(ProtoMapper.transformTags(ticketPb.getTagsList()));
//            tickets.setServiceId(ticketPb.getService().getServiceId());
        tickets.setAssignedEmployee(ProtoMapper.transformAssignedEmployee(ticketPb.getEmployeeAssigned()));
        tickets.setCustomerType(ticketPb.getCustomerType().name());
        tickets.setCreatedAt(ticketPb.getCreatedAt());
        tickets.setTicketType(type);
        tickets.setCreatedByName(ticketPb.getCreatedBy().getAccount().getFullName());
        tickets.setCreatedByPic(ticketPb.getCreatedBy().getAccount().getProfilePic());
        tickets.setTicketStatus(ticketPb.getTicketState().name());
        tickets.setPriority(ticketPb.getPriorityValue());
        return tickets;
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
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(Tickets.class)
                    .equalTo("ticketType", Constants.ASSIGNED)
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


    public List<Tickets> getSubscribedTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(Tickets.class)
                    .equalTo("ticketType", Constants.SUBSCRIBED)
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

    public List<Tickets> getClosedResolvedTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(Tickets.class)
                    .equalTo("ticketType", Constants.CLOSED_RESOLVED)
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

    public List<Tickets> getAssignableTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(Tickets.class)
                    .equalTo("ticketType", Constants.ASSIGNABLE)
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

    public List<Tickets> getSubscribeableTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(Tickets.class)
                    .equalTo("ticketType", Constants.SUBSCRIBEABLE)
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

    public void deleteTicket(long ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class).equalTo("ticketId", ticketId).findAll();
            result.deleteAllFromRealm();
        });
    }
}

