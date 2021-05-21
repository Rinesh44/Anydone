package com.anydone.desk.realm.repo;

import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Participant;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class ParticipantRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ParticipantRepo participantRepo;
    private static final String TAG = "ParticipantRepo";

    static {
        participantRepo = new ParticipantRepo();
    }

    public static ParticipantRepo getInstance() {
        return participantRepo;
    }

    public Participant getParticipantById(String participantId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Participant.class)
                    .equalTo("participantId", participantId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public void changeParticipantNotification(String participantId, String notificationType) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                RealmResults<Participant> participant = realm1.where(Participant.class)
                        .equalTo("participantId", participantId).findAll();
                participant.setString("notificationType", notificationType);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            close(realm);
        }
    }

    public Participant getParticipantByEmployeeId(String employeeId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Participant.class)
                    .equalTo("employee.assignEmployeeId", employeeId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public Participant getParticipantByEmployeeAccountId(String employeeId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Participant.class)
                    .equalTo("employee.accountId", employeeId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Participant> getParticipantsExcludingSelf(String inboxId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            Account userAccount = AccountRepo.getInstance().getAccount();
            return realm.where(Participant.class)
                    .equalTo("inboxId", inboxId)
                    .notEqualTo("employee.accountId", userAccount.getAccountId()).findAll();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Participant> searchParticipant(String inboxId, String query) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            query = query.replace("@", "").trim();
            GlobalUtils.showLog(TAG, "search query: " + query);
            return new ArrayList<>(realm.where(Participant.class)
                    .equalTo("inboxId", inboxId)
                    .contains("employee.name", query, Case.INSENSITIVE)
                    .findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

}
