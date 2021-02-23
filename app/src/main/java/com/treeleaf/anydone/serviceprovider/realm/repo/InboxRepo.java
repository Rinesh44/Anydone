package com.treeleaf.anydone.serviceprovider.realm.repo;


import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.DetectHtml;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class InboxRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final InboxRepo inboxRepo;
    private static final String TAG = "InboxRepo";

    static {
        inboxRepo = new InboxRepo();
    }

    public static InboxRepo getInstance() {
        return inboxRepo;
    }

    public void saveInbox(final InboxProto.Inbox inboxPb,
                          final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                Inbox inbox =
                        createNewInbox(inboxPb);
                realm1.copyToRealmOrUpdate(inbox);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void saveInboxes(final List<InboxProto.Inbox> inboxListPb,
                            final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                RealmList<Inbox> inboxList =
                        transformInbox(inboxListPb);
//                removeDeletedItems(threadList);
                if (!CollectionUtils.isEmpty(inboxList))
                    realm1.copyToRealmOrUpdate(inboxList);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void updateLastMsg(String inboxId, Conversation conversation, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Inbox> result = realm1.where(Inbox.class)
                        .equalTo("inboxId", inboxId).findAll();
                result.setString("lastMsg", conversation.getMessage());
                result.setString("lastMsgSender", conversation.getSenderName());
                result.setLong("lastMsgDate", conversation.getSentAt());
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void setParticipants(String inboxId, RealmList<Participant> participants,
                                final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Inbox> result = realm1.where(Inbox.class)
                        .equalTo("inboxId", inboxId).findAll();
                GlobalUtils.showLog(TAG, "participants: " + participants);
                result.setList("participantList", participants);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private RealmList<Inbox> transformInbox(List<InboxProto.Inbox> inboxListPb) {
        RealmList<Inbox> inboxRealmList = new RealmList<>();
        for (InboxProto.Inbox inbox : inboxListPb
        ) {
            Inbox inbox1 = createNewInbox(inbox);
            inboxRealmList.add(inbox1);
        }

        return inboxRealmList;
    }


    public void setAssignedEmployee(String inboxId, String participantId, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                AssignEmployee employee = AssignEmployeeRepo.getInstance().getAssignedEmployeeById(participantId);
                GlobalUtils.showLog(TAG, "get assigned emp det: " + employee);
                RealmResults<Thread> result = realm1.where(Thread.class)
                        .equalTo("threadId", inboxId).findAll();
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

    private void removeDeletedItems(RealmList<Thread> threadList) {
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        RealmResults<Inbox> oldThreadList = getInboxByServiceId(selectedService);
        oldThreadList.deleteAllFromRealm();
  /*      RealmList<Thread> diffList = new RealmList<>();
        for (Thread thread : oldThreadList
        ) {
            if (!threadList.contains(thread)) {
                diffList.add(thread);
            }
        }
        if (!CollectionUtils.isEmpty(diffList))
            diffList.deleteAllFromRealm();*/
    }


    public void enableBotReply(String threadId) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<Thread> result = realm1.where(Thread.class)
                    .equalTo("threadId", threadId).findAll();
            result.setBoolean("botEnabled", true);
        });
    }

    public void disableBotReply(String threadId) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<Thread> result = realm1.where(Thread.class)
                    .equalTo("threadId", threadId).findAll();
            result.setBoolean("botEnabled", false);
        });
    }


    public void setSeenStatus(Inbox inbox) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            GlobalUtils.showLog(TAG, "updateSeenStatus()");
            realm.executeTransaction(realm1 -> {
                inbox.setSeen(true);
                realm.copyToRealmOrUpdate(inbox);
            });
        } catch (Throwable throwable) {
            GlobalUtils.showLog(TAG, "error seen status update: " + throwable.getLocalizedMessage());
            throwable.printStackTrace();
        } finally {
            close(realm);
        }
    }

    public void setAssignedEmployee(String threadId, AssignEmployee employee, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Thread> result = realm1.where(Thread.class)
                        .equalTo("threadId", threadId).findAll();
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

    public void updateInbox(final Inbox inbox,
                            long updatedAt,
                            long lastMessageDate,
                            String lastMessage,
                            String lastMessageSender,
                            boolean seen,
                            final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            GlobalUtils.showLog(TAG, "updateInbox()");
            realm.executeTransaction(realm1 -> {
                inbox.setUpdatedAt(updatedAt);
                inbox.setLastMsgDate(lastMessageDate);
                inbox.setLastMsg(lastMessage);
                inbox.setLastMsgSender(lastMessageSender);
                inbox.setSeen(seen);
                realm.copyToRealmOrUpdate(inbox);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            GlobalUtils.showLog(TAG, "error inbox update: " + throwable.getLocalizedMessage());
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private Inbox createNewInbox(InboxProto.Inbox inboxPb) {
        Inbox newInbox = new Inbox();
        if (!CollectionUtils.isEmpty(inboxPb.getParticipantsList())) {
            newInbox.setParticipantList(transformParticipant(inboxPb.getId(), inboxPb.getParticipantsList()));
        }
        newInbox.setInboxId(inboxPb.getId());
//        newInbox.setServiceId(inboxPb.getServiceId());
        newInbox.setSubject(inboxPb.getSubject());
        newInbox.setCreatedByAccountType(inboxPb.getCreatedBy().getUser().getAccountType().name());
        newInbox.setSelfInbox(inboxPb.getSelfInbox());
        UserProto.User account = inboxPb.getCreatedBy().getUser();
        if (account.getAccountType().name().equalsIgnoreCase(AnydoneProto.AccountType.SERVICE_PROVIDER.name())) {
            newInbox.setCreatedByUserAccountId(inboxPb.getCreatedBy().getUser()
                    .getServiceProvider().getAccount().getAccountId());
            newInbox.setCreatedByUserAccountId(account.getServiceProvider().getAccount().getAccountId());
            newInbox.setCreatedByUserEmail(account.getServiceProvider().getAccount().getEmail());
            newInbox.setCreatedByUserAccountType(account.getServiceProvider().getAccount().getAccountType().name());
            newInbox.setCreatedByUserProfilePic(account.getServiceProvider().getAccount().getProfilePic());
            newInbox.setCreatedByUserFullName(account.getServiceProvider().getAccount().getFullName());
        } else if (account.getAccountType().name().equalsIgnoreCase
                (AnydoneProto.AccountType.SERVICE_CONSUMER.name())) {
            newInbox.setCreatedByUserAccountId(inboxPb.getCreatedBy().getUser()
                    .getConsumer().getAccount().getAccountId());
            newInbox.setCreatedByUserAccountId(account.getConsumer().getAccount().getAccountId());
            newInbox.setCreatedByUserEmail(account.getConsumer().getAccount().getEmail());
            newInbox.setCreatedByUserAccountType(account.getConsumer().getAccount().getAccountType().name());
            newInbox.setCreatedByUserProfilePic(account.getConsumer().getAccount().getProfilePic());
            newInbox.setCreatedByUserFullName(account.getConsumer().getAccount().getFullName());
        } else if (account.getAccountType().name().equalsIgnoreCase
                (AnydoneProto.AccountType.EMPLOYEE.name())) {
            newInbox.setCreatedByUserAccountId(inboxPb.getCreatedBy().getUser()
                    .getEmployee().getAccount().getAccountId());
            newInbox.setCreatedByUserAccountId(account.getEmployee().getAccount().getAccountId());
            newInbox.setCreatedByUserEmail(account.getEmployee().getAccount().getEmail());
            newInbox.setCreatedByUserAccountType(account.getEmployee().getAccount().getAccountType().name());
            newInbox.setCreatedByUserProfilePic(account.getEmployee().getAccount().getProfilePic());
            newInbox.setCreatedByUserFullName(account.getEmployee().getAccount().getFullName());
        } else {
            newInbox.setCreatedByUserAccountId(inboxPb.getCreatedBy().getUser()
                    .getAnydoneUser().getAccount().getAccountId());
            newInbox.setCreatedByUserAccountId(account.getAnydoneUser().getAccount().getAccountId());
            newInbox.setCreatedByUserEmail(account.getAnydoneUser().getAccount().getEmail());
            newInbox.setCreatedByUserAccountType(account.getAnydoneUser().getAccount().getAccountType().name());
            newInbox.setCreatedByUserProfilePic(account.getAnydoneUser().getAccount().getProfilePic());
            newInbox.setCreatedByUserFullName(account.getAnydoneUser().getAccount().getFullName());
        }


        newInbox.setCreatedAt(inboxPb.getCreatedAt());
        newInbox.setUpdatedAt(inboxPb.getUpdatedAt());
        setLastMsg(newInbox, inboxPb);
        newInbox.setSeen(inboxPb.getSeenStatus().getNumber() == RtcProto.RtcMessageStatus.SEEN_RTC_MSG_VALUE);
        newInbox.setLastMsgSender(inboxPb.getMessage().getSenderAccountObj().getFullName());
        newInbox.setLastMsgSenderId(inboxPb.getMessage().getSenderAccountObj().getAccountId());
        newInbox.setLastMsgType(inboxPb.getMessage().getRtcMessageType().name());
        if (inboxPb.getMessage().getSentAt() != 0)
            newInbox.setLastMsgDate(inboxPb.getMessage().getSentAt());
        else newInbox.setLastMsgDate(inboxPb.getCreatedAt());
        newInbox.setNotificationType(inboxPb.getNotificationType().name());
        return newInbox;
    }

    private void setLastMsg(Inbox inbox, InboxProto.Inbox inboxPb) {
        String lastMsg = inboxPb.getMessage().getText().getMessage();

        if (lastMsg.isEmpty()) {
             /*   tvLastMsg.setText("Attachment");
                tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_attachment_24,
                        0, 0, 0);
                tvLastMsg.setCompoundDrawablePadding(20);*/
            Account user = AccountRepo.getInstance().getAccount();
            switch (inboxPb.getMessage().getRtcMessageType().name()) {
                case "LINK_RTC_MESSAGE":
                    if (inbox.getLastMsgSenderId() != null)
                        if (inbox.getLastMsgSenderId().equals(user.getAccountId())) {
                            inbox.setLastMsg(("You: Sent a link"));
                        } else {
                            inbox.setLastMsg(inbox.getLastMsgSender() + ": Sent a link");
                        }
                    break;

                case "IMAGE_RTC_MESSAGE":
                    if (inbox.getLastMsgSenderId() != null)
                        if (inbox.getLastMsgSenderId().equals(user.getAccountId())) {
                            inbox.setLastMsg("You: Sent an image");
                        } else {
                            inbox.setLastMsg((inbox.getLastMsgSender() + ": Sent an image"));
                        }
                    break;

                case "DOC_RTC_MESSAGE":
                    if (inbox.getLastMsgSenderId() != null)
                        if (inbox.getLastMsgSenderId().equals(user.getAccountId())) {
                            inbox.setLastMsg(("You: Sent an attachment"));
                        } else {
                            inbox.setLastMsg(inbox.getLastMsgSender() + ": Sent an attachment");
                        }
                    break;
            }
        } else {
            Account user = AccountRepo.getInstance().getAccount();
            if (!lastMsg.isEmpty()) {
                boolean isHtml = DetectHtml.isHtml(lastMsg);
                if (isHtml) {
                    String senderId = inboxPb.getMessage().getSenderAccountObj().getAccountId();
                    String sender = inboxPb.getMessage().getSenderAccountObj().getFullName();
                    if (senderId != null) {
                        if (senderId.equals(user.getAccountId())) {
                            inbox.setLastMsg("You: " +
                                    Jsoup.parse(lastMsg).text());
                        } else {
                            inbox.setLastMsg(sender + ": " +
                                    Jsoup.parse(lastMsg).text());
                        }
                    } else inbox.setLastMsg("You: " +
                            Jsoup.parse(lastMsg).text());
                } else {
                    String senderId = inboxPb.getMessage().getSenderAccountObj().getAccountId();
                    String sender = inboxPb.getMessage().getSenderAccountObj().getFullName();
                    if (senderId != null) {
                        if (senderId.equals(user.getAccountId())) {
                            inbox.setLastMsg("You: " + lastMsg);
                        } else
                            inbox.setLastMsg(sender + ": " + lastMsg);
                    } else inbox.setLastMsg("You: " +
                            Jsoup.parse(lastMsg).text());
                }
            }

            if (lastMsg != null && !lastMsg.isEmpty()) {
                //change mentioned pattern to name
                String mentionPattern = "(?<=@)[\\w]+";
                Pattern p = Pattern.compile(mentionPattern);
                String msg = Jsoup.parse(lastMsg).text();
                Matcher m = p.matcher(msg);
//                    String changed = m.replaceAll("");
                while (m.find()) {
                    GlobalUtils.showLog(TAG, "found: " + m.group(0));
                    String employeeId = m.group(0);
                    Participant participant = ParticipantRepo.getInstance()
                            .getParticipantByEmployeeAccountId(employeeId);
                    if (participant != null && employeeId != null) {
                        msg = msg.replace(employeeId, participant.getEmployee().getName());
                        inbox.setLastMsg(msg);
                    }
                }
            }
        }

    }

    public RealmList<Participant> transformParticipant(String inboxId, List<InboxProto.InboxParticipant> participantListPb) {
        RealmList<Participant> participantList = new RealmList<>();
        for (InboxProto.InboxParticipant participantPb : participantListPb
        ) {
            AssignEmployee employee = new AssignEmployee();
            employee.setAccountId(participantPb.getUser().getEmployee().getAccount().getAccountId());
            employee.setCreatedAt(participantPb.getUser().getEmployee().getCreatedAt());
            employee.setEmail(participantPb.getUser().getEmployee().getAccount().getEmail());
            employee.setEmployeeId(participantPb.getUser().getEmployee().getEmployeeProfileId());
            employee.setEmployeeImageUrl(participantPb.getUser().getEmployee().getAccount().getProfilePic());
            employee.setPhone(participantPb.getUser().getEmployee().getAccount().getPhone());
            employee.setName(participantPb.getUser().getEmployee().getAccount().getFullName());

            GlobalUtils.showLog(TAG, "participant name: " + participantPb.getUser().getEmployee().getAccount().getFullName());
            GlobalUtils.showLog(TAG, "participant id: " + participantPb.getParticipantId());
            Participant participant = new Participant();
            participant.setAccountType(participantPb.getUser().getAccountType().name());
            participant.setEmployee(employee);
            participant.setParticipantId(participantPb.getParticipantId());
            participant.setRole(participantPb.getRole().name());
            participant.setNotificationType(participantPb.getNotificationType().name());
            participant.setInboxId(inboxId);

            participantList.add(participant);
        }
        return participantList;
    }


    public void updateThread(final Thread thread,
                             long updatedAt,
                             long lastMessageDate,
                             String lastMessage,
                             boolean seen,
                             final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            GlobalUtils.showLog(TAG, "updateThread()");
            realm.executeTransaction(realm1 -> {
                thread.setUpdatedAt(updatedAt);
                thread.setLastMessageDate(lastMessageDate);
                thread.setFinalMessage(lastMessage);
                thread.setSeen(seen);
                realm.copyToRealmOrUpdate(thread);
                callback.success(null);
            });
        } catch (Throwable throwable) {
            GlobalUtils.showLog(TAG, "error thread update: " + throwable.getLocalizedMessage());
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public Inbox getInboxById(String inboxId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Inbox.class)
                    .equalTo("inboxId", inboxId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public RealmResults<Inbox> getInboxByServiceId(String serviceId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Inbox.class)
                    .equalTo("serviceId", serviceId)
                    .sort("lastMsgDate", Sort.DESCENDING)
                    .findAll();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }


    public List<Inbox> getAllInbox() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return new ArrayList<>(realm.where(Inbox.class)
                    .sort("lastMsgDate", Sort.DESCENDING)
                    .findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public void changeMuteStatus(String inboxId, String muteNotificationType) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Inbox> result = realm1.where(Inbox.class)
                        .equalTo("inboxId", inboxId).findAll();
                result.setString("notificationType", muteNotificationType);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            close(realm);
        }
    }

    public List<Inbox> searchInbox(String query) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            GlobalUtils.showLog(TAG, "search query: " + query);
            return new ArrayList<>(realm.where(Inbox.class)
//                    .contains("participantList.employee.name", query, Case.INSENSITIVE)
                    .contains("subject", query, Case.INSENSITIVE)
                    .or()
                    .contains("participantList.employee.name", query, Case.INSENSITIVE)
                    .sort("lastMsgDate", Sort.DESCENDING)
                    .findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }


    public void deleteAllThreads() {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<Thread> result = realm1.where(Thread.class).findAll();
            result.deleteAllFromRealm();
        });
    }

    public void deleteInbox(String inboxId) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            RealmResults<Inbox> inbox = realm1.where(Inbox.class)
                    .equalTo("inboxId", inboxId).findAll();
            inbox.deleteAllFromRealm();
        });
    }

}
