package com.treeleaf.anydone.serviceprovider.realm.repo;


import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
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

    public void saveLinkedTicketList(final List<TicketProto.Ticket> ticketListPb,
                                     String type,
                                     String threadId,
                                     final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        try {
            realm.executeTransaction(realm1 -> {
                List<Tickets> ticketsList =
                        transformLinkedTicketProto(ticketListPb, type, threadId);
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

    public List<Tickets> getTicketByThreadId(String threadId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return realm.where(Tickets.class)
                    .equalTo("threadId", threadId).findAll();
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

    public void setContributors(long ticketId, RealmList<AssignEmployee> contributors,
                                final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> result = realm1.where(Tickets.class)
                        .equalTo("ticketId", ticketId).findAll();
                GlobalUtils.showLog(TAG, "contributors: " + contributors);
                result.setList("contributorList", contributors);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void editTeams(long ticketId, RealmList<Tags> teamRealmList, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> result = realm1.where(Tickets.class)
                        .equalTo("ticketId", ticketId).findAll();
                result.setList("tagsRealmList", teamRealmList);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void editLabels(long ticketId, RealmList<Label> labelList, final Callback
            callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> result = realm1.where(Tickets.class)
                        .equalTo("ticketId", ticketId).findAll();
                result.setList("labelRealmList", labelList);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void editTicketPriority(long ticketId, int priority) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            result.setInt("priority", priority);
        });
    }

    public void setTicketEstTime(long ticketId, long estTime) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            result.setLong("estimatedTimeStamp", estTime);
        });
    }

    public void deleteLabels(long ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            RealmList<Label> emptyList = new RealmList<>();
            result.setList("labelRealmList", emptyList);
        });
    }


    public void editTicketType(long ticketId, String ticketType) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            result.setString("ticketCategory", ticketType);
        });
    }

    public void editTicketTitle(long ticketId, String editedText) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            result.setString("title", editedText);
        });
    }

    public void editTicketDescription(long ticketId, String editedText) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            result.setString("description", editedText);
        });
    }

    public void editTicketEstimatedTime(long ticketId, String editedText, long estTime) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            result.setString("estimatedTime", editedText);
            result.setLong("estimatedTimeStamp", estTime);
        });
    }

    public void changeTicketStatusToStart(long ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            String status = TicketProto.TicketState.TICKET_STARTED.name();
            result.setString("ticketStatus", status);
            result.setString("ticketType", Constants.IN_PROGRESS);
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
            if (result.size() > 1) {
                result.deleteFirstFromRealm();
            }
        });
    }

    public void changeTicketStatusToReopened(long ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", ticketId).findAll();
            String status = TicketProto.TicketState.TICKET_REOPENED.name();
            result.setString("ticketStatus", status);
            result.setString("ticketType", Constants.PENDING);

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
            if (result.size() > 1) {
                result.deleteFirstFromRealm();
            }
        });
    }

    public void unAssignContributor(long ticketId, String empId, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            try {
                Tickets tickets = getTicketById(ticketId);
                AssignEmployee employeeToDel = null;
                for (AssignEmployee employee : tickets.getContributorList()
                ) {
                    if (employee.getEmployeeId().equalsIgnoreCase(empId)) {
                        employeeToDel = employee;
                    }
                }
                tickets.getContributorList().remove(employeeToDel);
                callback.success(null);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                callback.fail();
            }
        });
    }


    public void replaceAssignedEmployees(long ticketId, AssignEmployee employee, final Callback callback) {
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

    public void removeContributor(long ticketId, String contributorId, final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            try {
                RealmResults<Tickets> result = realm1.where(Tickets.class)
                        .equalTo("ticketId", ticketId).findAll();
                List<AssignEmployee> contributorsToRemove = new RealmList<>();
                for (Tickets ticket : result
                ) {
                    for (AssignEmployee contributor : ticket.getContributorList()
                    ) {
                        if (contributor.getEmployeeId().equalsIgnoreCase(contributorId)) {
                            contributorsToRemove.add(contributor);
                        }
                    }
                }

                for (Tickets tickets : result
                ) {
                    tickets.getContributorList().removeAll(contributorsToRemove);
                }

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
            tickets.setId(UUID.randomUUID().toString().replace("-", ""));
            tickets.setTicketId(ticketPb.getTicketId());
            tickets.setTitle(ticketPb.getTitle());
            tickets.setTicketCategory(ticketPb.getType().getName());
            GlobalUtils.showLog(TAG, "est time back end: " + ticketPb.getEstimatedTimeDesc());
            tickets.setEstimatedTime(ticketPb.getEstimatedTimeDesc());
            tickets.setDescription(ticketPb.getDescription());
            tickets.setTicketCategoryId(ticketPb.getType().getTicketTypeId());
            tickets.setCustomer(ProtoMapper.transformCustomer(ticketPb.getCustomer()));
            tickets.setServiceProvider(ProtoMapper.transformServiceProvider
                    (ticketPb.getServiceProvider()));
            tickets.setTicketSource(ticketPb.getTicketSource().name());
            tickets.setTagsRealmList(ProtoMapper.transformTags(ticketPb.getTeamsList()));
            tickets.setServiceId(ticketPb.getService().getServiceId());
            tickets.setAssignedEmployee(ProtoMapper.transformAssignedEmployee
                    (ticketPb.getEmployeeAssigned()));
            tickets.setLabelRealmList(ProtoMapper.transformLabels(ticketPb.getLabelList()));
            tickets.setCustomerType(ticketPb.getCustomerType().name());
            tickets.setEstimatedTimeStamp(ticketPb.getEstimatedTime());
            tickets.setCreatedAt(ticketPb.getCreatedAt());
            tickets.setTicketType(type);
            tickets.setPriority(ticketPb.getPriorityValue());
            tickets.setTicketStatus(ticketPb.getTicketState().name());
            tickets.setCreatedByName(ticketPb.getCreatedBy().getAccount().getFullName());
            tickets.setCreatedById(ticketPb.getCreatedBy().getAccount().getAccountId());
            tickets.setCreatedByPic(ticketPb.getCreatedBy().getAccount().getProfilePic());
            tickets.setBotEnabled(ticketPb.getIsBotEnabled());
            tickets.setContributorList(ProtoMapper.transformContributors
                    (ticketPb.getTicketContributorList()));
            ticketsList.add(tickets);
        }

        return ticketsList;
    }

    public List<Tickets> transformLinkedTicketProto
            (List<TicketProto.Ticket> ticketListPb, String type, String threadId) {
        if (CollectionUtils.isEmpty(ticketListPb)) {
            throw new IllegalArgumentException(EXCEPTION_NULL_VALUE);
        }

        List<Tickets> ticketsList = new ArrayList<>();
        for (TicketProto.Ticket ticketPb : ticketListPb
        ) {
            Tickets tickets = new Tickets();
            tickets.setId(UUID.randomUUID().toString().replace("-", ""));
            tickets.setTicketId(ticketPb.getTicketId());
            tickets.setThreadId(threadId);
            tickets.setTitle(ticketPb.getTitle());
            tickets.setTicketCategory(ticketPb.getType().getName());
            GlobalUtils.showLog(TAG, "est time back end: " + ticketPb.getEstimatedTimeDesc());
            tickets.setEstimatedTime(ticketPb.getEstimatedTimeDesc());
            tickets.setDescription(ticketPb.getDescription());
            tickets.setTicketCategoryId(ticketPb.getType().getTicketTypeId());
            tickets.setCustomer(ProtoMapper.transformCustomer(ticketPb.getCustomer()));
            tickets.setServiceProvider(ProtoMapper.transformServiceProvider
                    (ticketPb.getServiceProvider()));
            tickets.setTicketSource(ticketPb.getTicketSource().name());
            tickets.setTagsRealmList(ProtoMapper.transformTags(ticketPb.getTeamsList()));
            tickets.setServiceId(ticketPb.getService().getServiceId());
            tickets.setAssignedEmployee(ProtoMapper.transformAssignedEmployee
                    (ticketPb.getEmployeeAssigned()));
            tickets.setLabelRealmList(ProtoMapper.transformLabels(ticketPb.getLabelList()));
            tickets.setCustomerType(ticketPb.getCustomerType().name());
            tickets.setEstimatedTimeStamp(ticketPb.getEstimatedTime());
            tickets.setCreatedAt(ticketPb.getCreatedAt());
            tickets.setTicketType(type);
            tickets.setPriority(ticketPb.getPriorityValue());
            tickets.setTicketStatus(ticketPb.getTicketState().name());
            tickets.setCreatedByName(ticketPb.getCreatedBy().getAccount().getFullName());
            tickets.setCreatedById(ticketPb.getCreatedBy().getAccount().getAccountId());
            tickets.setCreatedByPic(ticketPb.getCreatedBy().getAccount().getProfilePic());
            tickets.setBotEnabled(ticketPb.getIsBotEnabled());
            tickets.setContributorList(ProtoMapper.transformContributors
                    (ticketPb.getTicketContributorList()));
            ticketsList.add(tickets);
        }

        return ticketsList;
    }

    public Tickets transformTicket
            (TicketProto.Ticket ticketPb, String type) {
        Account account = AccountRepo.getInstance().getAccount();
        Tickets tickets = new Tickets();
        tickets.setId(UUID.randomUUID().toString().replace("-", ""));
        tickets.setTicketId(ticketPb.getTicketId());
        tickets.setTitle(ticketPb.getTitle());
        tickets.setTicketCategory(ticketPb.getType().getName());
        tickets.setEstimatedTime(ticketPb.getEstimatedTimeDesc());
        tickets.setDescription(ticketPb.getDescription());
        tickets.setTicketCategoryId(ticketPb.getType().getTicketTypeId());
        tickets.setCustomer(ProtoMapper.transformCustomer(ticketPb.getCustomer()));
        tickets.setServiceProvider(ProtoMapper.transformServiceProvider
                (ticketPb.getServiceProvider()));
        tickets.setTicketSource(ticketPb.getTicketSource().name());
        tickets.setEstimatedTimeStamp(ticketPb.getEstimatedTime());
        tickets.setTagsRealmList(ProtoMapper.transformTags(ticketPb.getTeamsList()));
        tickets.setLabelRealmList(ProtoMapper.transformLabels(ticketPb.getLabelList()));
        tickets.setServiceId(ticketPb.getService().getServiceId());
        tickets.setAssignedEmployee(ProtoMapper.transformAssignedEmployee
                (ticketPb.getEmployeeAssigned()));
        tickets.setCustomerType(ticketPb.getCustomerType().name());
        tickets.setCreatedAt(ticketPb.getCreatedAt());
        tickets.setTicketType(type);
        tickets.setCreatedByName(account.getFullName());
        tickets.setCreatedById(ticketPb.getCreatedBy().getAccount().getAccountId());
        tickets.setCreatedByPic(account.getProfilePic());
        tickets.setTicketStatus(ticketPb.getTicketState().name());
        tickets.setPriority(ticketPb.getPriorityValue());
        tickets.setBotEnabled(ticketPb.getIsBotEnabled());
        tickets.setContributorList(ProtoMapper.transformContributors
                (ticketPb.getTicketContributorList()));
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

    public List<Tickets> getPendingTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(Tickets.class)
                    .equalTo("ticketType", Constants.PENDING)
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

    public List<Tickets> getInProgressTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(Tickets.class)
                    .equalTo("ticketType", Constants.IN_PROGRESS)
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

    public List<Tickets> getContributedTickets() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
            return new ArrayList<>(realm.where(Tickets.class)
                    .equalTo("ticketType", Constants.CONTRIBUTED)
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

    public void deletePendingTickets(final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> results = realm1.where(Tickets.class)
                        .equalTo("ticketType", Constants.PENDING)
                        .findAll();
                results.deleteAllFromRealm();
            });
        } catch (Throwable throwable) {
            GlobalUtils.showLog(TAG, "assigned ticket throwable: " + throwable.getLocalizedMessage());
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void deleteLinkedTickets(final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> results = realm1.where(Tickets.class)
                        .equalTo("ticketType", Constants.LINKED)
                        .findAll();
                results.deleteAllFromRealm();
            });
        } catch (Throwable throwable) {
            GlobalUtils.showLog(TAG, "assigned ticket throwable: " + throwable.getLocalizedMessage());
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void deleteInProgressTickets(final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> results = realm1.where(Tickets.class)
                        .equalTo("ticketType", Constants.IN_PROGRESS)
                        .findAll();
                results.deleteAllFromRealm();
            });
        } catch (Throwable throwable) {
            GlobalUtils.showLog(TAG, "assigned ticket throwable: " + throwable.getLocalizedMessage());
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void deleteAssignableTickets(final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> results = realm1.where(Tickets.class)
                        .equalTo("ticketType", Constants.ASSIGNABLE)
                        .findAll();
                results.deleteAllFromRealm();
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void deleteSubscribedTickets(final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> results = realm1.where(Tickets.class)
                        .equalTo("ticketType", Constants.SUBSCRIBED)
                        .findAll();
                results.deleteAllFromRealm();
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void deleteSubscribableTickets(final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> results = realm1.where(Tickets.class)
                        .equalTo("ticketType", Constants.SUBSCRIBEABLE)
                        .findAll();
                results.deleteAllFromRealm();
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void deleteClosedResolvedTickets(final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> results = realm1.where(Tickets.class)
                        .equalTo("ticketType", Constants.CLOSED_RESOLVED)
                        .findAll();
                results.deleteAllFromRealm();
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void deleteContributedTickets(final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Tickets> results = realm1.where(Tickets.class)
                        .equalTo("ticketType", Constants.CONTRIBUTED)
                        .findAll();
                results.deleteAllFromRealm();
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void enableBotReply(String ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", Long.parseLong(ticketId)).findAll();
            result.setBoolean("botEnabled", true);
        });
    }

    public void disableBotReply(String ticketId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> {
            RealmResults<Tickets> result = realm1.where(Tickets.class)
                    .equalTo("ticketId", Long.parseLong(ticketId)).findAll();
            result.setBoolean("botEnabled", false);
        });
    }
}

